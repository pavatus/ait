package loqor.ait.core.entities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public abstract class DummyCybermanEntity
        extends HostileEntity {
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2, false){

        @Override
        public void stop() {
            super.stop();
            DummyCybermanEntity.this.setAttacking(false);
        }

        @Override
        public void start() {
            super.start();
            DummyCybermanEntity.this.setAttacking(true);
        }
    };

    protected DummyCybermanEntity(EntityType<? extends DummyCybermanEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, VillagerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, WolfEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
    }

    public static DefaultAttributeContainer.Builder createDummyCybermanAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }

    protected abstract SoundEvent getStepSound();

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }
    @Override
    public void tickRiding() {
        super.tickRiding();
        Entity entity = this.getControllingVehicle();
        if (entity instanceof PathAwareEntity) {
            PathAwareEntity pathAwareEntity = (PathAwareEntity)entity;
            this.bodyYaw = pathAwareEntity.bodyYaw;
        }
    }

    @Override
    @Nullable public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        Random random = world.getRandom();
        this.setCanPickUpLoot(random.nextFloat() < 0.55f * difficulty.getClampedLocalDifficulty());
        return entityData;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.74f;
    }

    @Override
    public double getHeightOffset() {
        return -0.6;
    }

    public boolean isShaking() {
        return this.isFrozen();
    }
}
