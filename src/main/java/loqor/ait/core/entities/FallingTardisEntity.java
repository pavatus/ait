package loqor.ait.core.entities;

import com.mojang.logging.LogUtils;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.AITDamageTypes;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class FallingTardisEntity extends Entity {
	private static final Logger LOGGER = LogUtils.getLogger();
	private BlockState block;
	public int timeFalling;
	public boolean dropItem;
	private boolean destroyedOnLanding;
	private boolean hurtEntities;
	private int fallHurtMax;
	private float fallHurtAmount;
	@Nullable
	public NbtCompound blockEntityData;
	protected static final TrackedData<BlockPos> BLOCK_POS;
	protected static final TrackedData<Optional<UUID>> TARDIS_ID;

	public FallingTardisEntity(EntityType<? extends Entity> entityType, World world) {
		super(entityType, world);
		this.block = Blocks.SAND.getDefaultState();
		this.dropItem = true;
		this.fallHurtMax = 40;
	}

	private FallingTardisEntity(World world, double x, double y, double z, BlockState block) {
		this(AITEntityTypes.FALLING_TARDIS_TYPE, world);
		this.block = block;
		this.intersectionChecked = true;
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
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
		if (getTardisId() == null) {
			LOGGER.error("TARDIS ID WAS NULL AT " + getPos());
			return null;
		}

		if (getWorld().isClient) {
			return ClientTardisManager.getInstance().getLookup().get(getTardisId());
		}

		return ServerTardisManager.getInstance().getTardis(getTardisId());
	}

	public boolean isAttackable() {
		return false;
	}

	public void setFallingBlockPos(BlockPos pos) {
		this.dataTracker.set(BLOCK_POS, pos);
	}

	public BlockPos getFallingBlockPos() {
		return this.dataTracker.get(BLOCK_POS);
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

	public boolean canHit() {
		return !this.isRemoved();
	}

	private boolean wouldBeKilled() {
		return this.getBlockPos().down(2).getY() <= this.getWorld().getBottomY()
				|| (this.getBlockPos().up(2).getY() >= this.getWorld().getTopY() && this.getVelocity().y > 0.0);
	}

	public void tick() {
		if (this.block.isAir()) {
			this.discard();
		} else {
			Block block = this.block.getBlock();
			++this.timeFalling;
			if (!this.hasNoGravity()) {
				this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
			}

			if (this.wouldBeKilled()) {
				this.stopFalling(true);
				return;
			}

			this.move(MovementType.SELF, this.getVelocity());
			if (!this.getWorld().isClient) {
				if (getTardis() == null) return;
				this.getTardis().getDesktop().getConsoles().forEach(console -> {
					this.getWorld().playSound(null, console.position(), SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.BLOCKS, 1.0F, 1.0F);
				});
				if (PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED)) {
					stopFalling();
					return;
				}

				getTardis().getTravel().setPosition(new AbsoluteBlockPos.Directed(BlockPos.ofFloored(this.getPos()), this.getWorld(), this.getTardis().getTravel().getPosition().getDirection()));


				BlockPos blockPos = this.getBlockPos();
				if (blockPos == null) return;
				boolean bl = this.block.getBlock() instanceof ConcretePowderBlock;
				boolean bl2 = bl && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER);
				double d = this.getVelocity().lengthSquared();
				if (bl && d > 1.0) {
					BlockHitResult blockHitResult = this.getWorld().raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ), this.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this));
					if (blockHitResult.getType() != HitResult.Type.MISS && this.getWorld().getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
						blockPos = blockHitResult.getBlockPos();
						bl2 = true;
					}
				}

				if (!this.isOnGround() && !bl2) {
					if (!this.getWorld().isClient && (/*this.timeFalling > 100 &&*/ (blockPos.getY() <= this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopY()) /*|| this.timeFalling > 600*/)) {
						if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
							this.dropItem(block);
						}

						this.stopFalling();
					}
				} else {
					// This is when the tardis lands on its own

					boolean crashing = this.getTardis().getTravel().isCrashing();

					if (crashing) {
						World world = this.getWorld();
						BlockPos pos = this.getBlockPos();
						world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 10, true, World.ExplosionSourceType.MOB);
					}

					this.getTardis().getDoor().setLocked(crashing);

					this.stopFalling(false);

				}
			}

			if (this.getTardis().getTravel().isCrashing()) {
				this.setVelocity(this.getVelocity().multiply(1.5));
			} else {
				this.setVelocity(this.getVelocity().multiply(0.98));
			}

		}
	}

	public void stopFalling() {
		this.stopFalling(true);
	}

	public void stopFalling(boolean antigravs) {
		if (antigravs) {
			PropertiesHandler.set(getTardis(), PropertiesHandler.ANTIGRAVS_ENABLED, true);
		}
		TardisUtil.getPlayersInInterior(getTardis()).forEach(player -> {
			if (getTardis().getTravel().isCrashing()) {
				player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
				if (TardisUtil.isClient()) {
					ClientShakeUtil.shake(3.0f);
				}
			} else {
				player.playSound(AITSounds.LAND_THUD, 3.0f, 1.0f);
				if (TardisUtil.isClient()) {
					ClientShakeUtil.shake(0.5f);
				}
			}
		});
		if (TardisUtil.isServer()) {
			if (ForcedChunkUtil.isChunkForced((ServerWorld) this.getWorld(), this.getBlockPos())) {
				ForcedChunkUtil.stopForceLoading((ServerWorld) this.getWorld(), this.getBlockPos());
			}
		}
		if (getTardis().getTravel().isCrashing()) {
			getTardis().setLockedTardis(false);
		}
		getTardis().getTravel().setCrashing(false);


		Block block = this.block.getBlock();
		BlockPos blockPos = this.getBlockPos();
		boolean bl = this.block.getBlock() instanceof ConcretePowderBlock;
		boolean bl2 = bl && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER);
		double d = this.getVelocity().lengthSquared();
		BlockState blockState = this.getWorld().getBlockState(blockPos);
		this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
		if (!blockState.isOf(Blocks.MOVING_PISTON)) {
			if (!this.destroyedOnLanding) {
				boolean bl3 = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
				boolean bl4 = FallingBlock.canFallThrough(this.getWorld().getBlockState(blockPos.down())) && (!bl || !bl2);
				boolean bl5 = this.block.canPlaceAt(this.getWorld(), blockPos) && !bl4;

				if (this.block.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
					this.block = this.block.with(Properties.WATERLOGGED, true);
				}

				if (this.getWorld().setBlockState(blockPos, this.block, 3) && TardisUtil.isServer()) {
					((ServerWorld) this.getWorld()).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
					this.discard();
					if (block instanceof ExteriorBlock) {
						((ExteriorBlock) block).onLanding(this.getWorld(), blockPos, this.block, blockState, this);
					}

					if (this.blockEntityData != null && this.block.hasBlockEntity()) {
						BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
						if (blockEntity != null) {
							NbtCompound nbtCompound = blockEntity.createNbt();
							Iterator var13 = this.blockEntityData.getKeys().iterator();

							while (var13.hasNext()) {
								String string = (String) var13.next();
								nbtCompound.put(string, this.blockEntityData.get(string).copy());
							}

							try {
								blockEntity.readNbt(nbtCompound);
							} catch (Exception var15) {
								LOGGER.error("Failed to load block entity from falling block", var15);
							}

							blockEntity.markDirty();
						}
					}
				} else if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
					this.discard();
					this.onDestroyedOnLanding(block, blockPos);
					this.dropItem(block);
				}
			} else {
				this.discard();
				if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
					this.onDestroyedOnLanding(block, blockPos);
					this.dropItem(block);
				}
			}
		} else {
			this.discard();
			this.onDestroyedOnLanding(block, blockPos);
		}
	}

	public void onDestroyedOnLanding(Block block, BlockPos pos) {
		if (block instanceof LandingBlock) {
			// ((LandingBlock)block).onDestroyedOnLanding(this.getWorld(), pos, this);
		}
	}

	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		if (!this.hurtEntities) {
			return false;
		} else {
			int i = MathHelper.ceil(fallDistance - 1.0F);
			if (i < 0) {
				return false;
			} else {
				Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(EntityPredicates.VALID_LIVING_ENTITY);
				Block var8 = this.block.getBlock();
				DamageSource damageSource2 = AITDamageTypes.of(getWorld(), AITDamageTypes.TARDIS_SQUASH_DAMAGE_TYPE);
				float f = (float) Math.min(MathHelper.floor((float) i * this.fallHurtAmount), this.fallHurtMax);
				this.getWorld().getOtherEntities(this, this.getBoundingBox(), predicate).forEach((entity) -> {
					entity.damage(damageSource2, f);
				});
				boolean bl = this.block.isIn(BlockTags.ANVIL);
				if (bl && f > 0.0F && this.random.nextFloat() < 0.05F + (float) i * 0.05F) {
					BlockState blockState = AnvilBlock.getLandingState(this.block);
					if (blockState == null) {
						this.destroyedOnLanding = true;
					} else {
						this.block = blockState;
					}
				}

				return false;
			}
		}
	}

	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.put("BlockState", NbtHelper.fromBlockState(this.block));
		nbt.putInt("Time", this.timeFalling);
		nbt.putBoolean("DropItem", this.dropItem);
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

		if (nbt.contains("DropItem", 99)) {
			this.dropItem = nbt.getBoolean("DropItem");
		}

		if (nbt.contains("TileEntityData", 10)) {
			this.blockEntityData = nbt.getCompound("TileEntityData");
		}

		this.destroyedOnLanding = nbt.getBoolean("CancelDrop");
		if (this.block.isAir()) {
			this.block = Blocks.SAND.getDefaultState();
		}

	}

	public void setHurtEntities(float fallHurtAmount, int fallHurtMax) {
		this.hurtEntities = true;
		this.fallHurtAmount = fallHurtAmount;
		this.fallHurtMax = fallHurtMax;
	}

	public void setDestroyedOnLanding() {
		this.destroyedOnLanding = true;
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
