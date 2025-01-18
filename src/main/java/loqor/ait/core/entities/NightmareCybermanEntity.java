package loqor.ait.core.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;

public class NightmareCybermanEntity
        extends DummyCybermanEntity {
    //private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(NightmareCybermanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    //private int inPowderSnowTime;
    //private int conversionTime;

    public NightmareCybermanEntity(EntityType<? extends NightmareCybermanEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        //this.getDataTracker().startTracking(CONVERTING, false);
    }

    /*public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    public void setConverting(boolean converting) {
        this.dataTracker.set(CONVERTING, converting);
    }*/

    /*@Override
    public boolean isShaking() {
        return this.isConverting();
    }*/

    @Override
    public void tick() {
        /*if (!this.getWorld().isClient && this.isAlive() && !this.isAiDisabled()) {
            if (this.inPowderSnow) {
                if (this.isConverting()) {
                    --this.conversionTime;
                    if (this.conversionTime < 0) {
                        this.convertToStray();
                    }
                } else {
                    ++this.inPowderSnowTime;
                    if (this.inPowderSnowTime >= 140) {
                        this.setConversionTime(300);
                    }
                }
            } else {
                this.inPowderSnowTime = -1;
                this.setConverting(false);
            }
        }*/
        super.tick();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AITSounds.UPGRADE_IN_PROGRESS_ALT;
    }

    @Override
    protected SoundEvent getStepSound() {
        return AITSounds.STOMP1;
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        this.dropItem(AITItems.HANDLES);
    }
}