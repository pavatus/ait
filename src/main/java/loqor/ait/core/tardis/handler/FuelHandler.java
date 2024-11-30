package loqor.ait.core.tardis.handler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import loqor.ait.api.ArtronHolder;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.engine.impl.EmergencyPower;
import loqor.ait.core.tardis.dim.TardisDimension;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.world.RiftChunkManager;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;
import loqor.ait.data.properties.doubl3.DoubleProperty;
import loqor.ait.data.properties.doubl3.DoubleValue;

public class FuelHandler extends KeyedTardisComponent implements ArtronHolder, TardisTickable {

    public static final double TARDIS_MAX_FUEL = 50000;

    private static final DoubleProperty FUEL = new DoubleProperty("fuel", 1000d);
    private static final BoolProperty REFUELING = new BoolProperty("refueling", false);

    private final DoubleValue fuel = FUEL.create(this);
    private final BoolValue refueling = REFUELING.create(this);

    static {
        TardisEvents.DEMAT.register(tardis -> tardis.fuel().refueling().get() ? TardisEvents.Interaction.FAIL : TardisEvents.Interaction.PASS);
    }

    public FuelHandler() {
        super(Id.FUEL);
    }

    @Override
    public void onLoaded() {
        refueling.of(this, REFUELING);
        fuel.of(this, FUEL);
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
        this.fuel.set(MathHelper.clamp(fuel, 0, this.getMaxFuel()));

        if (this.isOutOfFuel() && prev != 0) {
            EmergencyPower backup = this.tardis().subsystems().emergency();
            if (backup.hasBackupPower()) {
                this.setCurrentFuel(backup.getCurrentFuel());
                backup.setCurrentFuel(0);
                TardisEvents.USE_BACKUP_POWER.invoker().onUse(this.tardis(), this.getCurrentFuel());
                return;
            }

            TardisEvents.OUT_OF_FUEL.invoker().onNoFuel(this.tardis);
        }
    }

    @Override
    public double addFuel(double var) {
        EmergencyPower backup = this.tardis().subsystems().emergency();
        if (backup.isEnabled() && !backup.isFull()) {
            return backup.addFuel(var);
        }

        return ArtronHolder.super.addFuel(var);
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

            if (manager.getArtron(chunk) > 0 && !TardisDimension.isTardisDimension(world)) {
                manager.removeFuel(chunk, 2);
                toAdd += 2;
            }

            this.addFuel(20 * toAdd);
        }

        if (!this.refueling().get() && tardis.engine().hasPower())
            this.removeFuel(20 * 0.25 * tardis.travel().instability());
    }

    public BoolValue refueling() {
        return refueling;
    }
}
