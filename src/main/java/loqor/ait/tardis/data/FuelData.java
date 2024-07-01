package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.ArtronHolder;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.managers.RiftChunkManager;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class FuelData extends TardisComponent implements ArtronHolder, TardisTickable {
	@Exclude
	public static final double TARDIS_MAX_FUEL = 50000;
	public static final String FUEL_COUNT = "fuel_count"; // todo this gets synced too much, needs changing.
	public static final String REFUELING = "refueling";

	public FuelData() {
		super(Id.FUEL);
	}

	private static void createFuelSyncDelay(Tardis tardis) {
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
	}

	@Override
	public double getCurrentFuel() {
		if (PropertiesHandler.get(tardis().properties(), FUEL_COUNT) == null) {
			AITMod.LOGGER.warn("Fuel count was null, setting to 0");
			this.setCurrentFuel(0);
		}

		return (double) PropertiesHandler.get(tardis().properties(), FUEL_COUNT);
	}

	@Override
	public void setCurrentFuel(double fuel) {
		double prev = getCurrentFuel();

		PropertiesHandler.set(tardis(), FUEL_COUNT, fuel, !isSyncOnDelay(tardis()));

		if (!isSyncOnDelay(tardis()))
			createFuelSyncDelay(tardis());

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

	public void setRefueling(boolean var) {
		PropertiesHandler.set(tardis(), REFUELING, var);
	}

	public boolean isRefueling() {
		return PropertiesHandler.getBool(tardis().properties(), REFUELING);
	}

	@Override
	public void tick(MinecraftServer server) {

		ServerTardis tardis = (ServerTardis) this.tardis();
		DirectedGlobalPos.Cached pos = tardis.travel().position();
		World world = pos.getWorld();
		TravelHandlerBase.State state = tardis.travel().getState();

		if (state == TravelHandlerBase.State.LANDED) {
			if (this.isRefueling() && this.getCurrentFuel() < FuelData.TARDIS_MAX_FUEL && (!isRefuelOnDelay(tardis))) {
				if (RiftChunkManager.isRiftChunk(pos.getPos()) && RiftChunkManager.getArtronLevels(world, pos.getPos()) > 0) {
					RiftChunkManager.setArtronLevels(world, pos.getPos(), RiftChunkManager.getArtronLevels(world, pos.getPos()) - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
					addFuel(9);
				} else {
					addFuel(7);
				}
				createRefuelDelay(tardis);
			}

			if (!isRefueling() && !isDrainOnDelay(tardis)) {
				createDrainDelay(tardis);
				removeFuel(0.25 * (tardis.tardisHammerAnnoyance + 1));
			}
		}

		if (state == TravelHandlerBase.State.FLIGHT) {
			if (!isDrainOnDelay(tardis)) {
				createDrainDelay(tardis);
				removeFuel((4 ^ (tardis.travel().speed())) * (tardis.tardisHammerAnnoyance + 1));
			}

			// TODO(travel): replace with proper travel method
			if (!tardis.engine().hasPower())
				  this.tardis.travel().crash(); // hehe force land if you don't have enough fuel
		}

		if ((state == TravelHandlerBase.State.DEMAT || state == TravelHandlerBase.State.MAT) && !isDrainOnDelay(tardis)) {
			createDrainDelay(tardis);
			removeFuel(5 * (tardis.tardisHammerAnnoyance + 1));
		}
	}
}
