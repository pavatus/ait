package loqor.ait.tardis.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

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

public class FuelHandler extends KeyedTardisComponent implements ArtronHolder, TardisTickable {

    public static final double TARDIS_MAX_FUEL = 50000;

    private static final DoubleProperty FUEL = new DoubleProperty("fuel", 1000d);
    private static final BoolProperty REFUELING = new BoolProperty("refueling", false);

    private final DoubleValue fuel = FUEL.create(this);
    private final BoolValue refueling = REFUELING.create(this);

    @Override
    public void onLoaded() {
        refueling.of(this, REFUELING);
        fuel.of(this, FUEL);
    }

    public FuelHandler() {
        super(Id.FUEL);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (server.getTicks() % 20 != 0)
            return;

        TravelHandler travel = this.tardis().travel();
        TravelHandlerBase.State state = travel.getState();

        switch (state) {
            case LANDED -> this.tickIdle();
            case FLIGHT -> this.tickFlight();
            case MAT, DEMAT -> this.tickMat();
        }
    }

    @Override
    public double getCurrentFuel() {
        return fuel.get();
    }

    @Override
    public void setCurrentFuel(double fuel) {
        double prev = this.getCurrentFuel();
        this.fuel.set(fuel);

        if (this.isOutOfFuel() && prev != 0)
            TardisEvents.OUT_OF_FUEL.invoker().onNoFuel(this.tardis);
    }

    @Override
    public double getMaxFuel() {
        return TARDIS_MAX_FUEL;
    }

    private void tickMat() {
        this.removeFuel(20 * 5 * this.tardis.travel().instability());
    }

    private void tickFlight() {
        TravelHandler travel = this.tardis.travel();
        this.removeFuel(20 * (4 ^ travel.speed()) * travel.instability());

        if (!tardis.engine().hasPower())
            travel.crash();
    }

    private void tickIdle() {
        if (this.refueling().get() && this.getCurrentFuel() < FuelHandler.TARDIS_MAX_FUEL) {
            TravelHandler travel = tardis.travel();

            DirectedGlobalPos.Cached pos = travel.position();
            ServerWorld world = pos.getWorld();

            RiftChunkManager manager = RiftChunkManager.getInstance(world);
            ChunkPos chunk = new ChunkPos(pos.getPos());

            double toAdd = 7;

            if (manager.getArtron(chunk) > 0)
                toAdd += (int) manager.removeFuel(chunk, 2);

            this.addFuel(20 * toAdd);
        }

        if (!this.refueling().get() && tardis.engine().hasPower())
            this.removeFuel(20 * 0.25 * tardis.travel().instability());
    }

    public BoolValue refueling() {
        return refueling;
    }
}
