package loqor.ait.core.entities;

import loqor.ait.api.link.LinkableLivingEntity;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.impl.DirectionControl;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.DirectedGlobalPos;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlightTardisEntity extends LinkableLivingEntity {

    private static final List<ItemStack> EMPTY = List.of();
    private static final ItemStack AIR = new ItemStack(Items.AIR);

    private Vec3d lastVelocity = Vec3d.ZERO;
    private BlockPos interiorPos;

    public FlightTardisEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);

        this.setInvulnerable(true);
        this.setNoGravity(true);
    }

    private FlightTardisEntity(World world, Vec3d pos, BlockPos riderPos, ServerTardis tardis) {
        this(AITEntityTypes.FLIGHT_TARDIS_TYPE, world);

        this.interiorPos = riderPos;

        this.link(tardis);
        this.setPosition(pos);
        this.setVelocity(Vec3d.ZERO);
    }

    public static void create(ServerPlayerEntity player, ServerTardis tardis) {
        TravelHandler travel = tardis.travel();

        DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();
        TardisUtil.teleportOutside(tardis, player);

        FlightTardisEntity tardisRealEntity = new FlightTardisEntity(
                exteriorPos.getWorld(), exteriorPos.getPos().toCenterPos(), player.getBlockPos(), tardis
        );

        tardisRealEntity.setRotation(RotationPropertyHelper.toDegrees(
                DirectionControl.getGeneralizedRotation(travel.position().getRotation())
        ), 0);

        exteriorPos.getWorld().spawnEntity(tardisRealEntity);
        player.startRiding(tardisRealEntity);
    }

    @Override
    public void tick() {
        super.tick();

        PlayerEntity player = this.getControllingPassenger();

        if (player.isMainPlayer()) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            client.options.hudHidden = true;
        }

        Tardis tardis = this.tardis();
        boolean onGround = this.isOnGround();

        if (player.isSneaking() && (onGround || tardis.travel().antigravs().get()))
            this.finishLand(tardis, player);

        // we do the check still, because isClientRiding may fail even if we're on client
        if (!onGround || this.getWorld().isClient())
            return;

        this.getWorld().playSound(null, this.getBlockPos(), AITSounds.LAND_THUD, SoundCategory.BLOCKS, 2F, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void finishLand(Tardis tardis, PlayerEntity player) {
        if (player.isMainPlayer()) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.setPerspective(Perspective.FIRST_PERSON);
            client.options.hudHidden = false;
            return;
        }

        // we do the check still, because isClientRiding may fail even if we're on client
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return;

        //RealTardisEntity.resetPlayer(serverPlayer);

        if (this.interiorPos == null) {
            TardisUtil.teleportInside(tardis, serverPlayer);
        } else {
            TardisUtil.teleportToInteriorPosition(tardis, serverPlayer, this.interiorPos);
        }

        //tardis.flight().land(player);
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

    @Nullable
    @Override
    public PlayerEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();

        if (entity instanceof PlayerEntity player)
            return player;

        return null;
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
}
