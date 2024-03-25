package loqor.ait.api.tardis;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class LinkableEntity extends Entity {
    public static final TrackedData<Optional<UUID>> TARDIS_ID;

    public LinkableEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    static {
        TARDIS_ID = DataTracker.registerData(LinkableEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
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

    public Tardis getTardis() {
        if (getTardisID() == null) {
            AITMod.LOGGER.warn("Tardis ID is null somehow?");
            return null;
        }

        if (TardisUtil.isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(getTardisID());
        }
        return ServerTardisManager.getInstance().getTardis(getTardisID());
    }

    public UUID getTardisID() {
        return this.dataTracker.get(TARDIS_ID).orElse(null);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TARDIS_ID, Optional.empty());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

}
