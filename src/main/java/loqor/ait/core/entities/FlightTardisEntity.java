package loqor.ait.core.entities;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Arm;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import loqor.ait.api.link.LinkableLivingEntity;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.impl.DirectionControl;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.mixin.rwf.LivingEntityAccessor;

public class FlightTardisEntity extends LinkableLivingEntity implements JumpingMount {

    private static final List<ItemStack> EMPTY = List.of();
    private static final ItemStack AIR = new ItemStack(Items.AIR);

    private Vec3d lastVelocity = Vec3d.ZERO;
    private BlockPos interiorPos;

    public FlightTardisEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);

        this.setInvulnerable(true);
    }

    private FlightTardisEntity(BlockPos riderPos, DirectedGlobalPos.Cached pos, ServerTardis tardis) {
        this(AITEntityTypes.FLIGHT_TARDIS_TYPE, pos.getWorld());

        this.interiorPos = riderPos;

        this.link(tardis);
        this.setPosition(pos.getPos().toCenterPos());
        this.setVelocity(Vec3d.ZERO);

        this.setRotation(RotationPropertyHelper.toDegrees(
                DirectionControl.getGeneralizedRotation(pos.getRotation())
        ), 0);
    }

    public static FlightTardisEntity createAndSpawn(ServerPlayerEntity player, ServerTardis tardis) {
        DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();

        FlightTardisEntity entity = new FlightTardisEntity(
                player.getBlockPos(), exteriorPos, tardis
        );

        exteriorPos.getWorld().spawnEntity(entity);
        return entity;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.lastVelocity = this.getVelocity();
            return;
        }

        PlayerEntity player = this.getPlayer();

        if (player == null || !this.isLinked())
            return;

        Tardis tardis = this.tardis().get();
        boolean onGround = this.isOnGround();

        tardis.flight().tickFlight((ServerPlayerEntity) player);

        if (tardis.door().isOpen()) {
            this.getWorld().getOtherEntities(this, this.getBoundingBox(), entity
                    -> !entity.isSpectator() && entity != player && entity instanceof LivingEntity).forEach(
                    entity -> TardisUtil.teleportInside(tardis, entity)
            );
        }

        if (player.isSneaking() && (onGround || tardis.travel().antigravs().get()))
            this.finishLand(tardis, player);
    }

    @Override
    public void setOnGround(boolean onGround, Vec3d movement) {
        if (!this.isOnGround() && onGround)
            this.playThud();

        super.setOnGround(onGround, movement);
    }

    @Override
    public void setOnGround(boolean onGround) {
        if (!this.isOnGround() && onGround)
            this.playThud();

        super.setOnGround(onGround);
    }

    private void playThud() {
        this.getWorld().playSound(null, this.getBlockPos(), AITSounds.LAND_THUD, SoundCategory.BLOCKS, 2F, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void finishLand(Tardis tardis, PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return;

        if (this.interiorPos == null) {
            TardisUtil.teleportInside(tardis, serverPlayer);
        } else {
            TardisUtil.teleportToInteriorPosition(tardis, serverPlayer, this.interiorPos);
        }

        tardis.flight().exitFlight(serverPlayer);
        this.discard();
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return EMPTY;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return AIR;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) { }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    public PlayerEntity getPlayer() {
        if (this.getControllingPassenger() instanceof PlayerEntity player)
            return player;

        return null;
    }

    @Nullable @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();

        if (entity instanceof LivingEntity living)
            return living;

        return null;
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 2f;
        float g = controllingPlayer.forwardSpeed * 2f;

        double v = ((LivingEntityAccessor) controllingPlayer).getJumping() ? 2
                : controllingPlayer.isSneaking() ? -1 : -0.1;

        if (v < 0 && this.isOnGround())
            return Vec3d.ZERO.add(0, -0.04f, 0);

        return new Vec3d(f, v, g);
    }

    @Override
    protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.5f;
    }

    public float getRotation(float tickDelta) {
        return ((float) this.age + tickDelta) / 20.0f;
    }

    public Vec3d lerpVelocity(float tickDelta) {
        return this.lastVelocity.lerp(this.getVelocity(), tickDelta);
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        Vec2f vec2f = new Vec2f(0, controllingPlayer.getYaw());
        this.setRotation(vec2f.y, vec2f.x);

        this.setBodyYaw(180.0f - this.getRotation(0.5f) / (float) Math.PI * 180f);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.interiorPos = BlockPos.fromLong(nbt.getLong("InteriorPos"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putLong("InteriorPos", this.interiorPos.asLong());
    }

    public static DefaultAttributeContainer.Builder createDummyAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 5);
    }

    @Override
    public void setJumpStrength(int strength) { }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void startJumping(int height) { }

    @Override
    public void stopJumping() { }
}
