package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.TardisTravel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class FlightData extends TardisLink {
	private static final String FLIGHT_TICKS_KEY = "flight_ticks";
	private static final String TARGET_TICKS_KEY = "target_ticks";
	private static final Random random = Random.create();

	public FlightData(Tardis tardiz) {
		super(tardiz, TypeId.FLIGHT);
		if (findTardis().isEmpty()) return;

		// todo this doesn't seem to work.
		TardisEvents.LANDED.register((tardis -> {
			if (this.findTardis().isEmpty()) return;
			if (!tardis.equals(this.findTardis().get())) return;

			this.setFlightTicks(0);
			this.setTargetTicks(0);
		}));
		TardisEvents.DEMAT.register((tardis -> {
			if (this.findTardis().isEmpty()) return false;
			if (!tardis.equals(this.findTardis().get())) return false;

			this.setFlightTicks(0);
			this.setTargetTicks(FlightUtil.getFlightDuration(tardis.getTravel().getPosition(), tardis.getTravel().getDestination()));

			return false;
		}));
	}

	private boolean isInFlight() {
		if (this.findTardis().isEmpty()) return false;
		return this.findTardis().get().getTravel().getState().equals(TardisTravel.State.FLIGHT) || this.findTardis().get().getTravel().getState().equals(TardisTravel.State.MAT);
	}

	private boolean isFlightTicking() {
		if (this.findTardis().isEmpty()) return false;
		return this.findTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT && this.getTargetTicks() != 0;
	}

	public boolean hasFinishedFlight() {
		if (findTardis().isEmpty()) return false;
		return (this.getFlightTicks() >= this.getTargetTicks() || this.getTargetTicks() == 0 ||
				findTardis().get().getTravel().isCrashing()) &&
				!PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
	}

	private void onFlightFinished() {
		if (this.findTardis().isEmpty()) return;

		this.setFlightTicks(0);
		this.setTargetTicks(0);

		FlightUtil.playSoundAtConsole(this.findTardis().get(), SoundEvents.BLOCK_BELL_RESONATE); // temp sound

		if (shouldAutoLand()) {
			this.findTardis().get().getTravel().materialise();
		}
	}

	private boolean shouldAutoLand() {
		if (this.findTardis().isEmpty()) return false;

		Tardis tardis = this.findTardis().get();

		return (PropertiesHandler.willAutoPilot(tardis.getHandlers().getProperties()) || !TardisUtil.isInteriorNotEmpty(tardis)) && !PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT); // todo im not too sure if this second check should exist, but its so funny ( ghost monument reference )
	}

	public void increaseFlightTime(int ticks) {
		this.setTargetTicks(this.getTargetTicks() + ticks);
	}

	public void decreaseFlightTime(int ticks) {
		this.setTargetTicks(this.getTargetTicks() - ticks);
	}

	public int getDurationAsPercentage() {
		if (this.findTardis().isEmpty()) return 0;
		if (this.getTargetTicks() == 0 || this.getFlightTicks() == 0) {
			if (this.findTardis().get().getTravel().getState() == TardisTravel.State.DEMAT) return 0;
			// if (this.tardis().getTravel().getState() == TardisTravel.State.MAT) return 100;
			return 100;
		}

		return FlightUtil.getDurationAsPercentage(this.getFlightTicks(), this.getTargetTicks());
	}

	public void recalculate() {
		if (findTardis().isEmpty()) return;
		this.setTargetTicks(FlightUtil.getFlightDuration(findTardis().get().position(), findTardis().get().destination()));
		this.setFlightTicks(this.isInFlight() ? MathHelper.clamp(this.getFlightTicks(), 0, this.getTargetTicks()) : 0);
	}

	public int getFlightTicks() {
		if (this.findTardis().isEmpty()) return 0;

		if (!this.findTardis().get().getHandlers().getProperties().getData().containsKey(FLIGHT_TICKS_KEY)) {
			this.setFlightTicks(0);
		}

		return PropertiesHandler.getInt(this.findTardis().get().getHandlers().getProperties(), FLIGHT_TICKS_KEY);
	}

	public void setFlightTicks(int ticks) {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), FLIGHT_TICKS_KEY, ticks);
	}

	public int getTargetTicks() {
		if (this.findTardis().isEmpty()) return 0;

		if (!this.findTardis().get().getHandlers().getProperties().getData().containsKey(TARGET_TICKS_KEY)) {
			this.setTargetTicks(0);
		}

		return PropertiesHandler.getInt(this.findTardis().get().getHandlers().getProperties(), TARGET_TICKS_KEY);
	}

	private void setTargetTicks(int ticks) {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), TARGET_TICKS_KEY, ticks);
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		if (this.findTardis().isEmpty()) return;

		ServerTardis tardis = (ServerTardis) this.findTardis().get();
		TardisCrashData crash = tardis.getHandlers().getCrashData();
		TardisTravel travel = tardis.getTravel();

		if (crash.getState() != TardisCrashData.State.NORMAL) {
			crash.addRepairTicks(2 * travel.getSpeed());
		}
		if ((this.getTargetTicks() > 0 || this.getFlightTicks() > 0) && travel.getState() == TardisTravel.State.LANDED) {
			this.recalculate();
		}

		triggerSequencingDuringFlight(tardis);

		if (this.isInFlight() && !travel.isCrashing() && !(this.getFlightTicks() >= this.getTargetTicks()) && this.getTargetTicks() == 0) {
			this.recalculate();
		}

		if (this.isFlightTicking()) {
			if (this.hasFinishedFlight()) {
				this.onFlightFinished();
			}

			this.setFlightTicks(this.getFlightTicks() + (Math.max(travel.getSpeed() / 2, 1)));
		}

		//System.out.println(PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT));

		if (!PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT) && this.isInFlight() && this.hasFinishedFlight() && !TardisUtil.isInteriorNotEmpty(tardis)) {
			travel.materialise();
		}
	}

	public void triggerSequencingDuringFlight(Tardis tardis) {
		SequenceHandler sequences = tardis.getHandlers().getSequenceHandler();
		TardisTravel travel = tardis.getTravel();

		if (!PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND)
				&& this.getDurationAsPercentage() < 100
				&& travel.inFlight() && tardis.position() != tardis.destination() && !sequences.hasActiveSequence()) {
			if (FlightUtil.getFlightDuration(tardis.position(),
					tardis.destination()) > FlightUtil.convertSecondsToTicks(5)) {
				int rand = random.nextBetween(0, 460 / (tardis.getTravel().getSpeed() == 0 ? 1 : tardis.getTravel().getSpeed()));
				if (rand == 7) {
					sequences.triggerRandomSequence(true);
				}
			}
		}
	}
}
