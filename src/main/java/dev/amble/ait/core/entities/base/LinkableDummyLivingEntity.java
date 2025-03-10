package dev.amble.ait.core.entities.base;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import dev.amble.ait.api.link.v2.TardisRef;
import dev.amble.ait.api.link.v2.entity.AbstractLinkableEntity;

public abstract class LinkableDummyLivingEntity extends DummyLivingEntity implements AbstractLinkableEntity {

    private static final TrackedData<Optional<UUID>> TARDIS = AbstractLinkableEntity
            .register(LinkableDummyLivingEntity.class);

    private TardisRef tardis;
    private final boolean writeNbt;

    public LinkableDummyLivingEntity(EntityType<? extends LivingEntity> type, World world, boolean writeNbt) {
        super(type, world);

        this.writeNbt = writeNbt;
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public DataTracker getDataTracker() {
        return super.getDataTracker();
    }

    @Override
    public TrackedData<Optional<UUID>> getTracked() {
        return TARDIS;
    }

    @Override
    public TardisRef asRef() {
        return this.tardis;
    }

    @Override
    public void setRef(TardisRef ref) {
        this.tardis = ref;
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        AbstractLinkableEntity.super.initDataTracker();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        AbstractLinkableEntity.super.onTrackedDataSet(data);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (this.writeNbt)
            super.readCustomDataFromNbt(nbt);

        AbstractLinkableEntity.super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.writeNbt)
            super.writeCustomDataToNbt(nbt);

        AbstractLinkableEntity.super.writeCustomDataToNbt(nbt);
    }
}
