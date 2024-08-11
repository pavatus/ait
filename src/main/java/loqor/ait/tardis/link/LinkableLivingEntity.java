package loqor.ait.tardis.link;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;

public abstract class LinkableLivingEntity extends LivingEntity {

    public static final TrackedData<Optional<UUID>> TARDIS_ID;

    static {
        TARDIS_ID = DataTracker.registerData(LinkableLivingEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    protected LinkableLivingEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("TardisID", this.getTardisID().toString());
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.dataTracker.set(TARDIS_ID, Optional.of(UUID.fromString(nbt.getString("TardisID"))));
    }

    public Tardis tardis() {
        UUID id = this.getTardisID();

        if (id == null)
            return null;

        return TardisManager.with(this, (o, manager) -> manager.demandTardis(o, id));
    }

    public UUID getTardisID() {
        return this.dataTracker.get(TARDIS_ID).orElse(null);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(TARDIS_ID, Optional.empty());
    }
}
