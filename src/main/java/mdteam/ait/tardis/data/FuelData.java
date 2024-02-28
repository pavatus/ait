package mdteam.ait.tardis.data;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.ArtronHolder;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.managers.RiftChunkManager;
import mdteam.ait.core.util.DeltaTimeManager;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class FuelData extends TardisLink implements ArtronHolder {
	@Exclude
	public static final double TARDIS_MAX_FUEL = 50000;
	public static final String FUEL_COUNT = "fuel_count"; // todo this gets synced too much, needs changing.
	public static final String REFUELING = "refueling";

	public FuelData(Tardis tardis) {
		super(tardis, "fuel");
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
		if (findTardis().isEmpty()) return 0;
		if (PropertiesHandler.get(findTardis().get().getHandlers().getProperties(), FUEL_COUNT) == null) {
			AITMod.LOGGER.warn("Fuel count was null, setting to 0");
			this.setCurrentFuel(0);
		}
		return (double) PropertiesHandler.get(findTardis().get().getHandlers().getProperties(), FUEL_COUNT);
	}

	@Override
	public void setCurrentFuel(double fuel) {
		if (findTardis().isEmpty()) return;
		double prev = getCurrentFuel();

		PropertiesHandler.set(findTardis().get(), FUEL_COUNT, fuel, !isSyncOnDelay(findTardis().get()));

		if (!isSyncOnDelay(findTardis().get())) {
			createFuelSyncDelay(findTardis().get());
		}

		// fire the event if ran out of fuel
		// this may get ran multiple times though for some reason
		if (isOutOfFuel() && prev != 0) {
			TardisEvents.OUT_OF_FUEL.invoker().onNoFuel(findTardis().get());
		}
	}

	@Override
	public double getMaxFuel() {
		return TARDIS_MAX_FUEL;
	}

	public void setRefueling(boolean var) {
		if (findTardis().isEmpty()) return;
		PropertiesHandler.set(findTardis().get(), REFUELING, var);
	}

	public boolean isRefueling() {
		if (findTardis().isEmpty()) return false;
		return PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), REFUELING);
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);
		if (findTardis().isEmpty()) return;

		// @TODO fix this because it seems that using any chunk references causes ticking to freak the hell out - Loqor

		ServerTardis tardis = (ServerTardis) this.findTardis().get();
		AbsoluteBlockPos pos = tardis.getTravel().getExteriorPos();
		World world = pos.getWorld();
		TardisTravel.State state = tardis.getTravel().getState();


		if (state == TardisTravel.State.LANDED && this.isRefueling() && this.getCurrentFuel() < FuelData.TARDIS_MAX_FUEL && (!isRefuelOnDelay(tardis))) {
			if (RiftChunkManager.isRiftChunk(pos) && RiftChunkManager.getArtronLevels(world, pos) > 0) {
				RiftChunkManager.setArtronLevels(world, pos, RiftChunkManager.getArtronLevels(world, pos) - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast one artron in the chunk
				addFuel(5);
			} else {
				addFuel(1);
			}
			createRefuelDelay(tardis);
		}
		if ((state == TardisTravel.State.DEMAT || state == TardisTravel.State.MAT) && !isDrainOnDelay(tardis)) {
			createDrainDelay(tardis);
			removeFuel(5 * (tardis.tardisHammerAnnoyance + 1));
		}
		if (state == TardisTravel.State.FLIGHT && !isDrainOnDelay(tardis)) {
			createDrainDelay(tardis);
			removeFuel((4 ^ (tardis.getTravel().getSpeed())) * (tardis.tardisHammerAnnoyance + 1));
		}
		if (state == TardisTravel.State.LANDED && !isRefueling() && !isDrainOnDelay(tardis)) {
			createDrainDelay(tardis);
			removeFuel(0.25 * (tardis.tardisHammerAnnoyance + 1));
		}
		if (state == TardisTravel.State.FLIGHT && !tardis.hasPower()) {
			findTardis().get().getTravel().crash(); // hehe force land if you don't have enough fuel
		}
	}
}
