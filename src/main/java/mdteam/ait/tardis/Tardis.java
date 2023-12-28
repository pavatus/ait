package mdteam.ait.tardis;

import mdteam.ait.client.util.ClientShakeUtil;
import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.handler.TardisHandlersManager;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.util.TardisChunkUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.server.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class Tardis {
    // this is starting to get a little bloated..

    private final TardisTravel travel;
    private final UUID uuid;
    private TardisDesktop desktop;
    private final TardisExterior exterior;
    private TardisHandlersManager handlers;
    private boolean dirty = false;

    @Exclude
    private static final double max_fuel = 5000;

    private double fuel_count = 0.00;
    private boolean refueling = true; // This is permanently true for now and has no penalty until @Loqor does the control stuff

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorSchema exteriorType, ExteriorVariantSchema variant) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), (tardis) -> new TardisExterior(tardis, exteriorType, variant), false);
    }

    protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, Function<Tardis, TardisExterior> exterior, boolean locked) {
        this.uuid = uuid;
        this.travel = travel.apply(this);
        this.desktop = desktop.apply(this);
        this.exterior = exterior.apply(this);
        this.handlers = new TardisHandlersManager(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDesktop(TardisDesktop desktop) {
        this.desktop = desktop;
    }

    public TardisDesktop getDesktop() {
        return desktop;
    }

    public TardisExterior getExterior() {
        return exterior;
    }

    public DoorHandler getDoor() {
        return this.getHandlers().getDoor();
    }

    // dont use this
    public void setLockedTardis(boolean bool) {
        this.getDoor().setLocked(bool);
    }

    public boolean getLockedTardis() {
        return this.getDoor().locked();
    }

    public TardisTravel getTravel() {
        return travel;
    }

    /**
     * Retrieves the TardisHandlersManager instance associated with the given UUID.
     *
     * @return TardisHandlersManager instance or null if it doesn't exist
     */
    public TardisHandlersManager getHandlers() {
        if (handlers == null) {
            handlers = new TardisHandlersManager(getUuid());
        }

        return handlers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;
        Tardis tardis = (Tardis) o;
        return uuid.equals(tardis.uuid);
    }

    public double getFuel() {
        return this.fuel_count;
    }

    public void setFuelCount(double fuel) {
        this.fuel_count = fuel;
    }

    public void addFuel(double fuel) {
        this.fuel_count += fuel;
    }

    public void removeFuel(double fuel) {
        if (this.fuel_count - fuel < 0) {
            this.fuel_count = 0;
        } else {
            this.fuel_count -= fuel;
        }
    }

    public void setRefueling(boolean isRefueling) {
        this.refueling = isRefueling;
    }

    public boolean isRefueling() {
        return this.refueling;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    /**
     * Called at the end of a servers tick
     *
     * @param server the server being ticked
     */
    public void tick(MinecraftServer server) {
        refueling = true;
        RiftChunk riftChunk = (RiftChunk) this.getTravel().getExteriorPos().getChunk();
        if (riftChunk.isRiftChunk() && getTravel().getState() == TardisTravel.State.LANDED && refueling && riftChunk.getArtronLevels() > 0 && fuel_count < max_fuel && (!DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getUuid().toString() + "-refueldelay"))) {
            riftChunk.setArtronLevels(riftChunk.getArtronLevels() - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
            addFuel(5);
            DeltaTimeManager.createDelay("tardis-" + getUuid().toString() + "-refueldelay", 250L);
        }
        if ((getTravel().getState() == TardisTravel.State.DEMAT || getTravel().getState() == TardisTravel.State.MAT) && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(3);
        }
        if (getTravel().getState() == TardisTravel.State.FLIGHT && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(1);
        }
        if (getTravel().getState() == TardisTravel.State.FLIGHT && fuel_count == 0) {
            getTravel().forceLand(); // hehe force land if you don't have enough fuel
        }
        this.getHandlers().tick(server);

        // im sure this is great for your server performace
        if (TardisChunkUtil.shouldExteriorChunkBeForced(this) && !TardisChunkUtil.isExteriorChunkForced(this)) {
            TardisChunkUtil.forceLoadExteriorChunk(this);
        } else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this) && TardisChunkUtil.isExteriorChunkForced(this)) {
            TardisChunkUtil.stopForceExteriorChunk(this);
        }

        // autoland stuff
        if (getTravel().getState() == TardisTravel.State.FLIGHT && PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.AUTO_LAND)) {
            getTravel().materialise();
        }

        // fixme nuh uh i dont like it when it locks on land it makes me sadge, instead lock if it was locked - Loqor

        /*if (PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.IS_FALLING) && !getHandlers().getDoor().locked()) {
            DoorHandler.lockTardis(true, this, null, true);
        }*/
        if (PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.IS_FALLING)) {
            DoorHandler.lockTardis(getHandlers().getDoor().locked(), this, null, true);
        }
    }

    /**
     * Called at the end of a worlds tick
     *
     * @param world the world being ticked
     */
    public void tick(ServerWorld world) {
    }

    /**
     * Called at the end of a clients tick, ONLY FOR CLIENT STUFF!!
     *
     * @param client the remote being ticked
     */
    public void tick(MinecraftClient client) { // fixme should likely be in ClientTardis instead, same with  other server-only things should be in ServerTardis
        // referencing client stuff where it COULD be server causes problems
        if(client.player != null &&
                TardisUtil.inBox(this.getDesktop().getCorners().getBox(), client.player.getBlockPos()) &&
                this.getTravel() != null && this.getTravel().getState() != TardisTravel.State.LANDED) {
            /*if (ClientShakeUtil.shouldShake(this)) */
            ClientShakeUtil.shakeFromConsole();
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }

    /**
     * Called at the START of a servers tick, ONLY to be used for syncing data to avoid comodification errors
     *
     * @param server the current minecraft server
     */
    public void startTick(MinecraftServer server) {
        if (this instanceof ServerTardis && isDirty()) {
            ((ServerTardis) this).sync();
            dirty = false;
        }
    }
}