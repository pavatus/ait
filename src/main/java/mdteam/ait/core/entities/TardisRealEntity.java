package mdteam.ait.core.entities;

import mdteam.ait.api.tardis.LinkableLivingEntity;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TardisRealEntity extends LinkableLivingEntity {

	public static final TrackedData<Optional<UUID>> PLAYER_UUID;
	public static final TrackedData<Optional<BlockPos>> PLAYER_INTERIOR_POSITION;
	protected Vec3d lastVelocity;
	private boolean shouldTriggerLandSound = false;

	public TardisRealEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
		this.lastVelocity = Vec3d.ZERO;
	}

	static {
		PLAYER_UUID = DataTracker.registerData(TardisRealEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
		PLAYER_INTERIOR_POSITION = DataTracker.registerData(TardisRealEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);
	}

	private TardisRealEntity(World world, UUID tardisID, double x, double y, double z, UUID playerUuid, BlockPos pos) {
		this(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, world);
		this.dataTracker.set(TARDIS_ID, Optional.of(tardisID));
		this.dataTracker.set(PLAYER_UUID, Optional.of(playerUuid));
		this.dataTracker.set(PLAYER_INTERIOR_POSITION, Optional.of(pos));
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
	}

	public static void spawnFromTardisId(World world, UUID tardisId, BlockPos spawnPos, PlayerEntity player, BlockPos pos) {
		if(world.isClient()) return;
		Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisId);
		TardisRealEntity tardisRealEntity = new TardisRealEntity(world, tardis.getUuid(), (double) spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getUuid(), pos);
		PropertiesHandler.set(tardis, PropertiesHandler.IS_IN_REAL_FLIGHT, true, true);
		world.spawnEntity(tardisRealEntity);
		tardisRealEntity.setRotation(tardis.getExterior().getExteriorPos().getDirection().asRotation(), 0);
		player.getAbilities().flying = true;
		player.getAbilities().allowFlying = true;
		tardis.getTravel().toFlight();
	}

	@Override
	public void tick() {
		this.lastVelocity = this.getVelocity();
		super.tick();
		if(this.getPlayer().isEmpty()) return;
		PlayerEntity user = this.getPlayer().get();

		boolean bl = PropertiesHandler.getBool(this.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
		user.startRiding(this);

		if (bl) {
			if (user.getWorld().isClient()) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
				client.options.hudHidden = true;
			} else {
				if(this.isOnGround()) {
					if(!shouldTriggerLandSound) {
						this.getWorld().playSound(null, this.getBlockPos(), AITSounds.LAND_THUD, SoundCategory.NEUTRAL, 2F, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
						user.getAbilities().flying = false;
						shouldTriggerLandSound = true;
					}
					if (user.isSneaking()) {
						getTardis().getTravel().setStateAndLand(new AbsoluteBlockPos.Directed(user.getBlockPos(), user.getWorld(), user.getHorizontalFacing()));
						if (getTardis().getTravel().getState() == TardisTravel.State.LANDED)
							PropertiesHandler.set(getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, false);
						user.dismountVehicle();
					}
				} else {
					shouldTriggerLandSound = false;
				}
			}
		} else if (!getTardis().getTravel().inFlight()) {
			if (user.getWorld().isClient()) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.options.setPerspective(Perspective.FIRST_PERSON);
				client.options.hudHidden = false;
			} else {
				user.clearStatusEffects();
				if(this.getPlayerBlockPos().isEmpty()) {
					TardisUtil.teleportInside(this.getTardis(), user);
				} else {
					TardisUtil.teleportToInteriorPosition(user, this.getPlayerBlockPos().get());
				}
				this.dataTracker.set(PLAYER_UUID, Optional.empty());
				this.discard();
			}
		}
	}

	public Vec3d lerpVelocity(float tickDelta) {
		return this.lastVelocity.lerp(this.getVelocity(), tickDelta);
	}

	@Override
	protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
		super.tickControlled(controllingPlayer, movementInput);

		Vec2f vec2f = this.getControlledRotation(controllingPlayer);
		this.setRotation(vec2f.y, vec2f.x);
	}

	protected Vec2f getControlledRotation(LivingEntity controllingPassenger) {
		return new Vec2f(0, controllingPassenger.getYaw());
	}

	@Override
	protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
		float f = controllingPlayer.sidewaysSpeed * 1.25f;
		float g = controllingPlayer.forwardSpeed * 1.25f;

		double v = controllingPlayer.getVelocity().y;
		if (v < 0 && !controllingPlayer.isSneaking())
			v = 0;

		return new Vec3d(f, v * 4f, g);
	}

	@Override
	protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
		return (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
	}

	@Override
	public double getMountedHeightOffset() {
		return 0.5f;
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		if(this.getPlayer().isEmpty()) return null;
		return this.getPlayer().get();
	}

	@Override
	public Arm getMainArm() {
		return null;
	}

	public float getRotation(float tickDelta) {
		return ((float) this.age + tickDelta) / 20.0f;
	}

	@Override
	public float getBodyYaw() {
		return 180.0f - this.getRotation(0.5f) / ((float) Math.PI * 2) * 360.0f;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if(this.getPlayer().isEmpty()) return nbt;
		nbt.putString("PlayerUuid", this.getPlayer().get().getUuid().toString());
		return nbt;
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		if (this.getPlayer().isPresent()) {
			if (player != this.getPlayer().get()) {
				if(this.getTardis().getDoor().isOpen()) {
					TardisUtil.teleportInside(this.getTardis(), player);
				}
			}
		}
		super.onPlayerCollision(player);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();

		this.dataTracker.startTracking(PLAYER_UUID, Optional.empty());
		this.dataTracker.startTracking(PLAYER_INTERIOR_POSITION, Optional.empty());
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return List.of();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return new ItemStack(Items.AIR);
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if(!nbt.contains("PlayerUuid")) return;
		this.dataTracker.set(PLAYER_UUID, Optional.of(UUID.fromString(nbt.getString("PlayerUuid"))));
	}

	public Optional<PlayerEntity> getPlayer() {
		if(this.getWorld() == null || this.dataTracker.get(PLAYER_UUID).isEmpty()) return Optional.empty();
		return Optional.ofNullable(this.getWorld().getPlayerByUuid(this.dataTracker.get(PLAYER_UUID).get()));
	}

	public Optional<BlockPos> getPlayerBlockPos() {
		if(this.getWorld() == null || this.dataTracker.get(PLAYER_INTERIOR_POSITION).isEmpty()) return Optional.empty();
		return Optional.of(this.dataTracker.get(PLAYER_INTERIOR_POSITION).get());
	}

	@Override
	public boolean hasNoGravity() {
		if(this.getPlayer().isEmpty()) return false;
        return this.getPlayer().get().getAbilities().flying;
    }

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	@Override
	public void setOnFire(boolean onFire) {
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return super.damage(source, 0);
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.INTENTIONALLY_EMPTY;
	}

	@Override
	protected void playBlockFallSound() {
	}

	@Override
	public FallSounds getFallSounds() {
		return new FallSounds(SoundEvents.INTENTIONALLY_EMPTY, SoundEvents.INTENTIONALLY_EMPTY);
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.INTENTIONALLY_EMPTY;
	}
}
