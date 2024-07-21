package loqor.ait.core.entities;

import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.entities.base.LinkableDummyLivingEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.link.v2.TardisRef;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Optional;
import java.util.UUID;

public class RealTardisEntity extends LinkableDummyLivingEntity {

    public static final TrackedData<Optional<UUID>> PLAYER_UUID = DataTracker.registerData(RealTardisEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public static final TrackedData<Optional<BlockPos>> PLAYER_INTERIOR_POSITION = DataTracker.registerData(RealTardisEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);

    private Vec3d lastVelocity = Vec3d.ZERO;

    public RealTardisEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    private RealTardisEntity(World world, Vec3d pos, UUID rider, BlockPos riderPos, Tardis tardis) {
        this(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, world);

        this.dataTracker.set(PLAYER_UUID, Optional.of(rider));
        this.dataTracker.set(PLAYER_INTERIOR_POSITION, Optional.of(riderPos));

        this.link(tardis);
        this.setPosition(pos);
        this.setVelocity(Vec3d.ZERO);
    }

    public static void create(World world, BlockPos spawnPos, ServerPlayerEntity player, Tardis tardis) {
        TravelHandler travel = tardis.travel();

        RealTardisEntity tardisRealEntity = new RealTardisEntity(
                world, spawnPos.toCenterPos(), player.getUuid(), player.getBlockPos(), tardis
        );

        tardisRealEntity.setRotation(RotationPropertyHelper.toDegrees(
                DirectionControl.getGeneralizedRotation(travel.position().getRotation())
        ), 0);

        world.spawnEntity(tardisRealEntity);
    }


    public static void create(ServerPlayerEntity player, Tardis tardis) {
        DirectedGlobalPos.Cached globalPos = tardis.travel().position();
        create(globalPos.getWorld(), globalPos.getPos(), player, tardis);
    }

    @Override
    public void tick() {
        super.tick();

        PlayerEntity player = this.getControllingPassenger();
        TardisRef tardisRef = this.tardis();

        if (player == null || tardisRef.isEmpty())
            return;

        player.startRiding(this);
        Tardis tardis = tardisRef.get();

        if (this.tardis().get().flight().isActive())
            this.flightTick(tardis, player);

        if (this.getWorld().isClient()) {
            this.lastVelocity = this.getVelocity();
            return;
        }

        if (!this.getWorld().isClient() && tardis.door().isOpen()) {
            this.getWorld().getOtherEntities(this, this.getBoundingBox(), entity
                    -> !entity.isSpectator() && entity instanceof LivingEntity).forEach(
                    entity -> TardisUtil.teleportInside(tardis, entity)
            );
        }
    }

    private void flightTick(Tardis tardis, PlayerEntity player) {
        if (this.isClientRiding(player)) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            client.options.hudHidden = true;
        }

        boolean onGround = this.isOnGround();

        if (player.isSneaking() && (onGround || tardis.travel().antigravs().get()))
            this.finishLand(tardis, player);

        // we do the check still, because isClientRiding may fail even if we're on client
        if (!onGround || this.getWorld().isClient())
            return;

        this.getWorld().playSound(null, this.getBlockPos(), AITSounds.LAND_THUD, SoundCategory.BLOCKS, 2F, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void finishLand(Tardis tardis, PlayerEntity player) {
        // TODO move this to RealFlightHandler
        if (this.isClientRiding(player)) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.setPerspective(Perspective.FIRST_PERSON);
            client.options.hudHidden = false;
            return;
        }

        // we do the check still, because isClientRiding may fail even if we're on client
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return;

        RealTardisEntity.resetPlayer(serverPlayer);

        if (this.getExitPos().isEmpty()) {
            TardisUtil.teleportInside(tardis, serverPlayer);
        } else {
            TardisUtil.teleportToInteriorPosition(tardis, serverPlayer, this.getExitPos().get());
        }

        tardis.flight().land(player);
        this.discard();
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        Vec2f vec2f = new Vec2f(0, controllingPlayer.getYaw());
        this.setRotation(vec2f.y, vec2f.x);
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 2f;
        float g = controllingPlayer.forwardSpeed * 2f;

        double v = controllingPlayer.getVelocity().y;
        if (v < 0 && !controllingPlayer.isSneaking())
            v = 0;

        return this.isOnGround() ? Vec3d.ZERO : new Vec3d(f, v * 4f, g);
    }

    @Override
    protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.5f;
    }

    @Nullable
    @Override
    public PlayerEntity getControllingPassenger() {
        return this.getPlayer().orElse(null);
    }

    @Override
    public float getBodyYaw() {
        return 180.0f - this.getRotation(0.5f) / (float) Math.PI * 180f;
    }

    @Override
    public boolean hasNoGravity() {
        return this.getPlayer().map(player -> player.getAbilities().flying).orElse(false);
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(PLAYER_UUID, Optional.empty());
        this.dataTracker.startTracking(PLAYER_INTERIOR_POSITION, Optional.empty());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (!nbt.contains("PlayerUuid"))
            return;

        this.dataTracker.set(PLAYER_UUID, Optional.of(nbt.getUuid("PlayerUuid")));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (!nbt.contains("PlayerUuid") || this.getPlayer().isEmpty())
            return;

        nbt.putUuid("PlayerUuid", this.getPlayer().get().getUuid());
    }

    public float getRotation(float tickDelta) {
        return ((float) this.age + tickDelta) / 20.0f;
    }

    public Vec3d lerpVelocity(float tickDelta) {
        return this.lastVelocity.lerp(this.getVelocity(), tickDelta);
    }

    public Optional<PlayerEntity> getPlayer() {
        if (this.getWorld() == null)
            return Optional.empty();

        Optional<UUID> targetPlayerId = this.dataTracker.get(PLAYER_UUID);

        if (targetPlayerId.isEmpty())
            return Optional.empty();

        UUID uuid = targetPlayerId.get();

        if (!this.getWorld().isClient())
            return Optional.ofNullable(this.getWorld().getPlayerByUuid(uuid));

        if (this.isClientRiding(uuid))
            return Optional.ofNullable(MinecraftClient.getInstance().player);

        return Optional.empty();
    }

    public Optional<BlockPos> getExitPos() {
        if (this.getWorld() == null || this.dataTracker.get(PLAYER_INTERIOR_POSITION).isEmpty())
            return Optional.empty();

        return Optional.of(this.dataTracker.get(PLAYER_INTERIOR_POSITION).get());
    }

    private boolean isClientRiding(PlayerEntity player) {
        if (!this.getWorld().isClient())
            return false;

        PlayerEntity client = getClientPlayer();
        return client == player || isClientRiding(player.getUuid());
    }

    private boolean isClientRiding(UUID uuid) {
        if (!this.getWorld().isClient())
            return false;

        PlayerEntity client = getClientPlayer();
        return client != null && client.getUuid().equals(uuid);
    }

    @Environment(EnvType.CLIENT)
    private static PlayerEntity getClientPlayer() {
        return MinecraftClient.getInstance().player;
    }

    // TODO move to RealFlightHandler 
    private static void resetPlayer(ServerPlayerEntity player) {
        PlayerAbilities abilities = player.getAbilities();

        abilities.invulnerable = player.isCreative();
        abilities.allowFlying = player.isCreative();
        abilities.flying = false;

        player.sendAbilitiesUpdate();
    }
}
