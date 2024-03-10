package mdteam.ait.core.entities;

import mdteam.ait.api.tardis.LinkableLivingEntity;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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

	public TardisRealEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	static {
		PLAYER_UUID = DataTracker.registerData(TardisRealEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	}

	private TardisRealEntity(World world, UUID tardisID, double x, double y, double z, UUID playerUuid) {
		this(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, world);
		this.dataTracker.set(TARDIS_ID, Optional.of(tardisID));
		this.dataTracker.set(PLAYER_UUID, Optional.of(playerUuid));
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);

		this.getPlayer().get().startRiding(this);
	}

	public static void spawnFromTardisId(World world, UUID tardisId, BlockPos spawnPos, PlayerEntity player) {
		if(world.isClient()) return;
		Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisId);
		TardisRealEntity tardisRealEntity = new TardisRealEntity(world, tardis.getUuid(), (double) spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getUuid());
		PropertiesHandler.set(tardis, PropertiesHandler.IS_IN_REAL_FLIGHT, true, true);
		world.spawnEntity(tardisRealEntity);
		tardis.getTravel().toFlight();
		tardisRealEntity.setRotation(tardis.getExterior().getExteriorPos().getDirection().asRotation(), 0);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.getPlayer().isEmpty()) return;
		PlayerEntity user = this.getPlayer().get();
		user.getAbilities().flying = true;

		boolean bl =  PropertiesHandler.getBool(this.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT);

		if (bl) {
			if (user.getWorld().isClient()) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
				client.options.hudHidden = true;
			} else {
				if (user.isSneaking() && this.isOnGround()) {
					getTardis().getTravel().setStateAndLand(new AbsoluteBlockPos.Directed(user.getBlockPos(), user.getWorld(), user.getHorizontalFacing()));
					if(getTardis().getTravel().getState() == TardisTravel.State.LANDED) PropertiesHandler.set(getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, false);
				}
			}
		} else if (!getTardis().getTravel().inFlight()) {
			if (user.getWorld().isClient()) {
				MinecraftClient client = MinecraftClient.getInstance();
				client.options.setPerspective(Perspective.FIRST_PERSON);
				client.options.hudHidden = false;
			} else {
				user.clearStatusEffects();
				TardisUtil.teleportInside(getTardis(), user);
				this.dataTracker.set(PLAYER_UUID, Optional.empty());
				this.discard();
			}
		}
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
		float f = controllingPlayer.sidewaysSpeed;
		float g = controllingPlayer.forwardSpeed;

		double v = controllingPlayer.getVelocity().y;
		if (v < 0 && !controllingPlayer.isSneaking())
			v = 0;

		return new Vec3d(f, v * 4f, g);
	}

	@Override
	protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
		return (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		return this.getPlayer().get();
	}

	@Override
	public Arm getMainArm() {
		return null;
	}

	public float getRotation(float tickDelta) {
		return ((float) this.age + tickDelta) / 10.0f;
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
	protected void initDataTracker() {
		super.initDataTracker();

		this.dataTracker.startTracking(PLAYER_UUID, Optional.empty());
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

	@Override
	public boolean hasNoGravity() {
		return true;
	}
}
