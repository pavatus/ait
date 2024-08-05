package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.ArtronHolder;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.managers.RiftChunkManager;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class FuelHandler extends KeyedTardisComponent implements ArtronHolder, TardisTickable {
	@Exclude
	public static final double TARDIS_MAX_FUEL = 50000;
	private static final Property<Double> FUEL_COUNT_PROPERTY = new Property<>(Property.Type.DOUBLE, "fuel_count", 1000d);
	private final Value<Double> fuelCount = FUEL_COUNT_PROPERTY.create(this);
	private static final BoolProperty IS_REFUELING = new BoolProperty("is_refueling", false);
	private final BoolValue isRefueling = IS_REFUELING.create(this);
	@Override
	public void onLoaded() {
		isRefueling.of(this, IS_REFUELING);
		fuelCount.of(this, FUEL_COUNT_PROPERTY);
	}

	public FuelHandler() {
		super(Id.FUEL);
	}

	/*private static void createFuelSyncDelay(Tardis tardis) {
		DeltaTimeManager.createDelay(tardis.getUuid() + "-fuel-sync", 5 * 1000L);
	}

	private static boolean isSyncOnDelay(Tardis tardis) {
		return DeltaTimeManager.isStillWaitingOnDelay(tardis.getUuid() + "-fuel-sync");
	}

	private static void createRefuelDelay(Tardis tardis) {
		DeltaTimeManager.createDelay("tardis-" + tardis.getUuid().toString() + "-refueldelay", 250L);
	}

	private static boolean isRefuelOnDelay(Tardis tardis) {
		return DeltaTimeManager.isStillWaitingOnDelay("tardis-" + tardis.getUuid().toString() + "-refueldelay");
	}

	private static void createDrainDelay(Tardis tardis) {
		DeltaTimeManager.createDelay("tardis-" + tardis.getUuid().toString() + "-fueldraindelay", 500L);
	}

	private static boolean isDrainOnDelay(Tardis tardis) {
		return DeltaTimeManager.isStillWaitingOnDelay("tardis-" + tardis.getUuid().toString() + "-fueldraindelay");
	}*/

	@Override
	public double getCurrentFuel() {
		if (fuelCount.get() == null) {
			AITMod.LOGGER.warn("Fuel count was null, setting to 0");
			this.setCurrentFuel(0);
		}

		return fuelCount.get();
	}

	@Override
	public void setCurrentFuel(double fuel) {
		double prev = getCurrentFuel();

		fuelCount.set(fuel);

		// if (!isSyncOnDelay(tardis()))
		// 	createFuelSyncDelay(tardis());

		// fire the event if ran out of fuel
		// this may get ran multiple times though for some reason
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
			if (this.getRefueling().get() && this.getCurrentFuel() < FuelHandler.TARDIS_MAX_FUEL /*&& (!isRefuelOnDelay(tardis))*/) {
				if (RiftChunkManager.isRiftChunk(pos.getPos()) && RiftChunkManager.getArtronLevels(world, pos.getPos()) > 0) {
					RiftChunkManager.setArtronLevels(world, pos.getPos(), RiftChunkManager.getArtronLevels(world, pos.getPos()) - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
					addFuel(9);
				} else {
					addFuel(7);
				}
				// createRefuelDelay(tardis);
			}

			if (!getRefueling().get() /* && !isDrainOnDelay(tardis)*/) {
				// createDrainDelay(tardis);
				removeFuel(0.25 * travel.instability());
			}
		}

		if (state == TravelHandlerBase.State.FLIGHT) {
			//if (!isDrainOnDelay(tardis)) {
			//	createDrainDelay(tardis);
				removeFuel((4 ^ travel.speed()) * travel.instability());
			//}

			// TODO: make a crash method to avoid isGrowth checks outside of interiorchanginghandler
			if (!tardis.engine().hasPower() && !tardis.isGrowth())
				  travel.crash(); // hehe force land if you don't have enough fuel
		}

		if ((state == TravelHandlerBase.State.DEMAT || state == TravelHandlerBase.State.MAT)/* && !isDrainOnDelay(tardis)*/) {
			//createDrainDelay(tardis);
			removeFuel(5 * travel.instability());
		}
	}
}
