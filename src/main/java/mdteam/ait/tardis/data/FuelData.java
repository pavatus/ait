package mdteam.ait.tardis.data;

import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.server.MinecraftServer;

public class FuelData extends TardisLink {
    @Exclude
    public static final double TARDIS_MAX_FUEL = 25000;
    public static final String FUEL_COUNT = "fuel_count";
    public static final String REFUELING = "refueling";
    private double fuel;
    private boolean refueling;

    public FuelData(Tardis tardis) {
        super(tardis, "fuel");
    }

    public double getFuel() {
        return fuel;
    }

    public boolean isOutOfFuel() {
        return getFuel() <= 0;
    }

    public void setFuelCount(double fuel) {
        double prev = getFuel();

        this.fuel = fuel;
        if(getTardis().isEmpty()) return;

        // fire the event if ran out of fuel
        // this may get ran multiple times though for some reason
        if (isOutOfFuel() && prev != 0) {
            TardisEvents.OUT_OF_FUEL.invoker().onNoFuel(getTardis().get());
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
        this.refueling = isRefueling;
        this.sync();
    }

    // is always true for now
    public boolean isRefueling() {
        return this.refueling;
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if(getTardis().isEmpty()) return;

        // creativious i moved your code here
        RiftChunk riftChunk = (RiftChunk) this.getTardis().get().getTravel().getExteriorPos().getChunk();
        if (getTardis().get().getTravel().getState() == TardisTravel.State.LANDED && this.isRefueling() && riftChunk.getArtronLevels() > 0 && this.getFuel() < FuelData.TARDIS_MAX_FUEL && (!DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().get().getUuid().toString() + "-refueldelay"))) {
            if(riftChunk.isRiftChunk()) {
                riftChunk.setArtronLevels(riftChunk.getArtronLevels() - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
                addFuel(5);
            } else {
                addFuel(1);
            }
            DeltaTimeManager.createDelay("tardis-" + getTardis().get().getUuid().toString() + "-refueldelay", 250L);
        }
        if ((getTardis().get().getTravel().getState() == TardisTravel.State.DEMAT || getTardis().get().getTravel().getState() == TardisTravel.State.MAT) && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().get().getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getTardis().get().getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(5);
        }
        if (getTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().get().getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getTardis().get().getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(4^(this.getTardis().get().getTravel().getSpeed()));
        }
        if (getTardis().get().getTravel().getState() == TardisTravel.State.LANDED && !isRefueling() && !DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().get().getUuid().toString() + "-fueldraindelay")) {
            DeltaTimeManager.createDelay("tardis-" + getTardis().get().getUuid().toString() + "-fueldraindelay", 500L);
            removeFuel(0.25);
        }
        if (getTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT && !this.getTardis().get().hasPower()) {
            getTardis().get().getTravel().crash(); // hehe force land if you don't have enough fuel
        }
    }
}
