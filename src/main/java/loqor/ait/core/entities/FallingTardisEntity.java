package loqor.ait.core.entities;

import loqor.ait.AITMod;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITDamageTypes;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

// FIXME doesnt update position in travel properly
public class FallingTardisEntity extends Entity {
	public int timeFalling;
	private boolean destroyedOnLanding;
	private boolean hurtEntities;
	private BlockState block;
	@Nullable
	public NbtCompound blockEntityData;
	protected static final TrackedData<BlockPos> BLOCK_POS;
	protected static final TrackedData<Optional<UUID>> TARDIS_ID;

	private int fallHurtMax;
	private float fallHurtAmount;

	public FallingTardisEntity(EntityType<? extends Entity> entityType, World world) {
		super(entityType, world);
		this.fallHurtMax = 40;
		this.block = AITBlocks.EXTERIOR_BLOCK.getDefaultState();
	}

	private FallingTardisEntity(World world, double x, double y, double z, BlockState block) {
		this(AITEntityTypes.FALLING_TARDIS_TYPE, world);
		this.intersectionChecked = true;
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.block = block;
		this.setFallingBlockPos(this.getBlockPos());
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putString("TardisId", getTardisId().toString());
		nbt.put("BlockPos", NbtHelper.fromBlockPos(this.getBlockPos()));
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.setTardisId(UUID.fromString(nbt.getString("TardisId")));
		this.setBlockPos(NbtHelper.toBlockPos(nbt.getCompound("BlockPos")));
	}

	private void setBlockPos(BlockPos blockPos) {
		this.dataTracker.set(BLOCK_POS, blockPos);
	}

	public static FallingTardisEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
		FallingTardisEntity fallingBlockEntity = new FallingTardisEntity(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, false) : state);

		if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior) {
			if (exterior.findTardis().isEmpty()) return null;
			fallingBlockEntity.setTardisId(exterior.findTardis().get().getUuid());

			PropertiesHandler.set(exterior.findTardis().get(), PropertiesHandler.IS_FALLING, true);
		}

		world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
		world.spawnEntity(fallingBlockEntity);
		fallingBlockEntity.setHurtEntities(100.0F, 100);
		return fallingBlockEntity;
	}

	public Tardis getTardis() {
		if (this.getTardisId() == null) {
			AITMod.LOGGER.error("TARDIS ID WAS NULL AT " + getPos());
			return null;
		}

		return TardisManager.with(this, (o, manager) -> manager.demandTardis(o, this.getTardisId()));
	}

	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		return true;
	}

	public void setFallingBlockPos(BlockPos pos) {
		this.dataTracker.set(BLOCK_POS, pos);
	}

	protected Entity.MoveEffect getMoveEffect() {
		return MoveEffect.NONE;
	}

	public UUID getTardisId() {
		return this.dataTracker.get(TARDIS_ID).orElse(null);
	}

	public void setTardisId(UUID uuid) {
		this.dataTracker.set(TARDIS_ID, Optional.of(uuid));
	}

	protected void initDataTracker() {
		this.dataTracker.startTracking(BLOCK_POS, BlockPos.ORIGIN);
		this.dataTracker.startTracking(TARDIS_ID, Optional.empty());
	}

	@Override
	protected void tickInVoid() {
		super.tickInVoid();
		this.stopFalling(true);
	}

	public void tick() {
		if (this.block.isAir()) {
			this.discard();
		} else {
            this.timeFalling++;

			if (!this.hasNoGravity())
				this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));

			this.move(MovementType.SELF, this.getVelocity());

			Tardis tardis = this.getTardis();

			if (tardis == null)
				return;

			if (!this.getWorld().isClient()) {
				this.getTardis().getDesktop().getConsolePos().forEach(console -> this.getWorld().playSound(
						null, console, SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.BLOCKS, 1.0F, 1.0F)
				);

				if (PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED)) {
					this.stopFalling();
					return;
				}

				tardis.travel().setPosition(new AbsoluteBlockPos.Directed(BlockPos.ofFloored(this.getPos()), this.getWorld(),
						DirectionControl.getGeneralizedRotation(this.getTardis().travel().getPosition().getRotation())));

				BlockPos blockPos = this.getBlockPos();

				if (blockPos == null)
					return;

                if (!this.isOnGround()) {
					if((blockPos.getY() <= this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopY())) {
						this.stopFalling();
					}
				} else {
					boolean crashing = tardis.travel().isCrashing();

					if (crashing) {
						this.getWorld().createExplosion(this, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
								10, true, World.ExplosionSourceType.MOB
						);
					}
					tardis.getDoor().setLocked(crashing);
					this.stopFalling(false);
				}
			}

			this.setVelocity(this.getVelocity().multiply(tardis.travel().isCrashing() ? 1.5f : 0.98f));
		}
	}

	public void stopFalling() {
		this.stopFalling(true);
	}

	public void stopFalling(boolean antigravs) {
		if (antigravs)
			PropertiesHandler.set(this.getTardis(), PropertiesHandler.ANTIGRAVS_ENABLED, true);

		Tardis tardis = this.getTardis();
		boolean isCrashing = tardis.travel().isCrashing();

		if (isCrashing)
			tardis.setLockedTardis(false);

		TardisUtil.getPlayersInInterior(tardis).forEach(player -> {
			SoundEvent sound = isCrashing ? SoundEvents.ENTITY_GENERIC_EXPLODE : AITSounds.LAND_THUD;
			float volume = isCrashing ? 1.0F : 3.0F;

			player.playSound(sound, volume, 1.0f);

			if (this.getWorld().isClient())
				ClientShakeUtil.shake(isCrashing ? 3.0f : 0.5f);
		});

		if (!this.getWorld().isClient() && ForcedChunkUtil.isChunkForced((ServerWorld) this.getWorld(), this.getBlockPos()))
			ForcedChunkUtil.stopForceLoading((ServerWorld) this.getWorld(), this.getBlockPos());

		tardis.travel().setCrashing(false);

		Block block = this.block.getBlock();
		BlockPos blockPos = this.getBlockPos();

        BlockState blockState = this.getWorld().getBlockState(blockPos);
		this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));

		if (!blockState.isOf(Blocks.MOVING_PISTON) && !this.destroyedOnLanding) {
			if (this.block.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
				this.block = this.block.with(Properties.WATERLOGGED, true);
			}

			if (this.getWorld().setBlockState(blockPos, this.block, 3) && !this.getWorld().isClient()) {
				((ServerWorld) this.getWorld()).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
				this.discard();

				if (block instanceof ExteriorBlock) {
					((ExteriorBlock) block).onLanding(this.getWorld(), blockPos, this.block, blockState, this);
				}

				if (this.blockEntityData != null && this.block.hasBlockEntity()) {
					BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);

					if (blockEntity == null)
						return;

                    NbtCompound nbt = blockEntity.createNbt();

                    for (String string : this.blockEntityData.getKeys()) {
                        nbt.put(string, this.blockEntityData.get(string).copy());
                    }

                    try {
                        blockEntity.readNbt(nbt);
                    } catch (Exception e) {
                        AITMod.LOGGER.error("Failed to load block entity from falling tardis", e);
                    }

                    blockEntity.markDirty();
                }
			}
		} else {
			this.discard();
		}
	}

	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(fallDistance - 1.0F);
            if (i >= 0) {
                Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(EntityPredicates.VALID_LIVING_ENTITY);
                DamageSource damageSource2 = AITDamageTypes.of(getWorld(), AITDamageTypes.TARDIS_SQUASH_DAMAGE_TYPE);
                float f = (float) Math.min(MathHelper.floor((float) i * this.fallHurtAmount), this.fallHurtMax);

                this.getWorld().getOtherEntities(this, this.getBoundingBox(), predicate)
						.forEach((entity) -> entity.damage(damageSource2, f));
            }
        }

        return false;
    }

	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.put("BlockState", NbtHelper.fromBlockState(this.block));
		nbt.putInt("Time", this.timeFalling);
		nbt.putBoolean("HurtEntities", this.hurtEntities);
		nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
		nbt.putInt("FallHurtMax", this.fallHurtMax);
		if (this.blockEntityData != null) {
			nbt.put("TileEntityData", this.blockEntityData);
		}

		nbt.putBoolean("CancelDrop", this.destroyedOnLanding);
	}

	protected void readCustomDataFromNbt(NbtCompound nbt) {
		this.block = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("BlockState"));
		this.timeFalling = nbt.getInt("Time");
		if (nbt.contains("HurtEntities", 99)) {
			this.hurtEntities = nbt.getBoolean("HurtEntities");
			this.fallHurtAmount = nbt.getFloat("FallHurtAmount");
			this.fallHurtMax = nbt.getInt("FallHurtMax");
		} else if (this.block.isIn(BlockTags.ANVIL)) {
			this.hurtEntities = true;
		}

		if (nbt.contains("TileEntityData", 10)) {
			this.blockEntityData = nbt.getCompound("TileEntityData");
		}

		this.destroyedOnLanding = nbt.getBoolean("CancelDrop");

		if (this.block.isAir()) {
			this.block = AITBlocks.EXTERIOR_BLOCK.getDefaultState();
		}
	}

	public void setHurtEntities(float fallHurtAmount, int fallHurtMax) {
		this.hurtEntities = true;
		this.fallHurtAmount = fallHurtAmount;
		this.fallHurtMax = fallHurtMax;
	}

	public boolean doesRenderOnFire() {
		return false;
	}

	public void populateCrashReport(CrashReportSection section) {
		super.populateCrashReport(section);
		section.add("Immitating BlockState", this.block.toString());
	}

	public BlockState getBlockState() {
		return this.block;
	}

	protected Text getDefaultName() {
		return Text.translatable("entity.minecraft.falling_block_type", this.block.getBlock().getName());
	}

	public boolean entityDataRequiresOperator() {
		return true;
	}

	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
	}

	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.block = Block.getStateFromRawId(packet.getEntityData());
		this.intersectionChecked = true;
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		this.setPosition(d, e, f);
		this.setFallingBlockPos(this.getBlockPos());
	}

	static {
		BLOCK_POS = DataTracker.registerData(FallingTardisEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
		TARDIS_ID = DataTracker.registerData(FallingTardisEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	}
}
