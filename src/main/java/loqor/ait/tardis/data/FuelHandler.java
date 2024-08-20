package loqor.ait.tardis.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import loqor.ait.api.tardis.ArtronHolder;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.properties.doubl3.DoubleProperty;
import loqor.ait.tardis.data.properties.doubl3.DoubleValue;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class FuelHandler extends KeyedTardisComponent implements ArtronHolder, TardisTickable {

    public static final double TARDIS_MAX_FUEL = 50000;

    private static final DoubleProperty FUEL = new DoubleProperty("fuel_count", 1000d);
    private static final BoolProperty IS_REFUELING = new BoolProperty("is_refueling", false);

    private final DoubleValue fuelCount = FUEL.create(this);
    private final BoolValue isRefueling = IS_REFUELING.create(this);

    @Override
    public void onLoaded() {
        isRefueling.of(this, IS_REFUELING);
        fuelCount.of(this, FUEL);
    }

    public FuelHandler() {
        super(Id.FUEL);
    }

    @Override
    public double getCurrentFuel() {
        return fuelCount.get();
    }

    @Override
    public void setCurrentFuel(double fuel) {
        double prev = getCurrentFuel();

        fuelCount.set(fuel);

        if (isOutOfFuel() && prev != 0) {
            TardisEvents.OUT_OF_FUEL.invoker().onNoFuel(tardis());
        }
    }

    @Override
    public double getMaxFuel() {
        return TARDIS_MAX_FUEL;
    }

    public BoolValue getRefueling() {
        return isRefueling;
    }

    @Override
    public void tick(MinecraftServer server) {
        ServerTardis tardis = (ServerTardis) this.tardis();
        TravelHandler travel = tardis.travel();

        DirectedGlobalPos.Cached pos = travel.position();
        World world = pos.getWorld();

        TravelHandlerBase.State state = travel.getState();

        if (state == TravelHandlerBase.State.LANDED) {
            if (this.getRefueling().get() && this.getCurrentFuel() < FuelHandler.TARDIS_MAX_FUEL) {
                RiftChunkManager manager = RiftChunkManager.getInstance((ServerWorld) this.world);
                ChunkPos chunk = new ChunkPos(pos.getPos());

                if (manager.getArtron(chunk) > 0) {
                    manager.removeFuel(chunk, 2);

                    addFuel(9);
                } else {
                    addFuel(7);
                }
            }

            if (!getRefueling().get() && tardis.engine().hasPower()) {
                removeFuel(0.25 * travel.instability());
            }
        }

        if (state == TravelHandlerBase.State.FLIGHT) {
            removeFuel((4 ^ travel.speed()) * travel.instability());

            // TODO: make a crash method to avoid isGrowth checks outside of
            // interiorchanginghandler
            if (!tardis.engine().hasPower() && !tardis.isGrowth())
                travel.crash(); // hehe force land if you don't have enough fuel
        }

        if ((state == TravelHandlerBase.State.DEMAT
                || state == TravelHandlerBase.State.MAT) /* && !isDrainOnDelay(tardis) */) {
            removeFuel(5 * travel.instability());
        }
    }
}
