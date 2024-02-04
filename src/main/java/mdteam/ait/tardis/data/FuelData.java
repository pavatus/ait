package mdteam.ait.tardis.data;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.core.managers.RiftChunkManager;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FuelData extends TardisLink {
    @Exclude
    public static final double TARDIS_MAX_FUEL = 35000;
    public static final String FUEL_COUNT = "fuel_count"; // todo this gets synced too much, needs changing.
    public static final String REFUELING = "refueling";

    public FuelData(Tardis tardis) {
        super(tardis, "fuel");
    }

    private static void createFuelSyncData(Tardis tardis) {
        DeltaTimeManager.createDelay(tardis.getUuid() + "-fuel-sync", 5 * 1000L);
    }
    private static boolean isSyncOnDelay(Tardis tardis) {
        return DeltaTimeManager.isStillWaitingOnDelay(tardis.getUuid() + "-fuel-sync");
    }

    public double getFuel() {
        if(getTardis().isEmpty()) return 0;
        if (PropertiesHandler.get(getTardis().get().getHandlers().getProperties(), FUEL_COUNT) == null) {
            AITMod.LOGGER.warn("Fuel count was null, setting to 0");
            this.setFuelCount(0);
        }
        return (double) PropertiesHandler.get(getTardis().get().getHandlers().getProperties(), FUEL_COUNT);
    }

    public boolean isOutOfFuel() {
        return getFuel() <= 0;
    }

    public void setFuelCount(double fuel) {
        if(getTardis().isEmpty()) return;
        double prev = getFuel();

        PropertiesHandler.set(getTardis().get(), FUEL_COUNT, fuel, !isSyncOnDelay(getTardis().get()));

        if (!isSyncOnDelay(getTardis().get())) {
            createFuelSyncData(getTardis().get());
        }

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

    public void setRefueling(boolean var) {
        if(getTardis().isEmpty()) return;
        PropertiesHandler.set(getTardis().get(), REFUELING, var);
    }

    public boolean isRefueling() {
        if(getTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(getTardis().get().getHandlers().getProperties(), REFUELING);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if(getTardis().isEmpty()) return;

        // @TODO fix this because it seems that using any chunk references causes ticking to freak the hell out - Loqor
        BlockPos pos = this.getTardis().get().getTravel().getExteriorPos();
        World world = this.getTardis().get().getTravel().getExteriorPos().getWorld();
        if (getTardis().get().getTravel().getState() == TardisTravel.State.LANDED && this.isRefueling() && this.getFuel() < FuelData.TARDIS_MAX_FUEL && (!DeltaTimeManager.isStillWaitingOnDelay("tardis-" + getTardis().get().getUuid().toString() + "-refueldelay"))) {
            if(RiftChunkManager.isRiftChunk(pos) && RiftChunkManager.getArtronLevels(world, pos) > 0) {
                RiftChunkManager.setArtronLevels(world, pos, RiftChunkManager.getArtronLevels(world, pos) - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
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
