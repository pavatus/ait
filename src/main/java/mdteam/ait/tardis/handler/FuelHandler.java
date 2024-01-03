package mdteam.ait.tardis.handler;

import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class FuelHandler extends TardisLink {
    @Exclude
    public static final double TARDIS_MAX_FUEL = 25000;
    public static final String FUEL_COUNT = "fuel_count";
    public static final String REFUELING = "refueling";

    public FuelHandler(UUID tardisId) {
        super(tardisId);
    }

    public double getFuel() {
        return (double) PropertiesHandler.get(getTardis().getHandlers().getProperties(), FUEL_COUNT);
    }

    public boolean isOutOfFuel() {
        return getFuel() <= 0;
    }

    public void setFuelCount(double fuel) {
        double prev = getFuel();

        PropertiesHandler.set(getTardis().getHandlers().getProperties(), FUEL_COUNT, fuel);
        getTardis().markDirty();

        // fire the event if ran out of fuel
        // this may get ran multiple times though for some reason
        if (isOutOfFuel() && prev != 0) {
            TardisEvents.OUT_OF_FUEL.invoker().onNoFuel(getTardis());
        }
    }

    public double addFuel(double fuel) {
        double currentFuel = this.getFuel();
        this.setFuelCount(this.getFuel() <= TARDIS_MAX_FUEL ? this.getFuel() + fuel : TARDIS_MAX_FUEL);
        if(this.getFuel() == TARDIS_MAX_FUEL)
            return fuel - (TARDIS_MAX_FUEL - currentFuel);
        return 0;
    }

    public void removeFuel(double fuel) {
        if (this.getFuel() - fuel < 0) {
            this.setFuelCount(0);
            return;
        }
        this.setFuelCount(getFuel() - fuel);
    }

    public void setRefueling(boolean isRefueling) {
        PropertiesHandler.setBool(getTardis().getHandlers().getProperties(), REFUELING, isRefueling);
        getTardis().markDirty();
    }

    // is always true for now
    public boolean isRefueling() {
        return PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), REFUELING);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        // creativious i moved your code here
        RiftChunk riftChunk = (RiftChunk) this.getTardis().getTravel().getExteriorPos().getChunk();
        if (getTardis().getTravel().getState() == TardisTravel.State.LANDED && this.isRefueling() && riftChunk.getArtronLevels() > 0 && this.getFuel() < FuelHandler.TARDIS_MAX_FUEL && (!DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().getUuid().toString() + "-refueldelay"))) {
            if(riftChunk.isRiftChunk()) {
                riftChunk.setArtronLevels(riftChunk.getArtronLevels() - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
                addFuel(5);
            } else {
                addFuel(1);
            }
            DeltaTimeManager.createDelay("tardis-" + getTardis().getUuid().toString() + "-refueldelay", 250L);
        }
        if ((getTardis().getTravel().getState() == TardisTravel.State.DEMAT || getTardis().getTravel().getState() == TardisTravel.State.MAT) && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getTardis().getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(5);
        }
        if (getTardis().getTravel().getState() == TardisTravel.State.FLIGHT && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getTardis().getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(3);
        }
        if (getTardis().getTravel().getState() == TardisTravel.State.LANDED && !isRefueling() && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getTardis().getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(0.25);
        }
        if (getTardis().getTravel().getState() == TardisTravel.State.FLIGHT && !this.getTardis().hasPower()) {
            getTardis().getTravel().crash(); // hehe force land if you don't have enough fuel
        }
    }
}
