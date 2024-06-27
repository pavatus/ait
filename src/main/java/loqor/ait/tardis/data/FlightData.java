package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

// TODO move the flight-travel calculations stuff to TravelHandler
public class FlightData extends KeyedTardisComponent implements TardisTickable {
	private static final String FLIGHT_TICKS_KEY = "flight_ticks";
	private static final String TARGET_TICKS_KEY = "target_ticks";
	private static final Random random = Random.create();

	private static final BoolProperty HANDBRAKE = new BoolProperty("handbrake", true);
	private static final BoolProperty AUTOPILOT = new BoolProperty("autopilot", false);

	private final BoolValue handbrake = HANDBRAKE.create(this);
	private final BoolValue autopilot = AUTOPILOT.create(this);

	public FlightData() {
		super(Id.FLIGHT);
	}

	static {
		TardisEvents.LANDED.register(tardis -> {
			FlightData flight = tardis.flight();

			flight.setFlightTicks(0);
			flight.setTargetTicks(0);
		});

		TardisEvents.DEMAT.register(tardis -> {
			FlightData flight = tardis.flight();
			TravelHandlerBase travel = tardis.travel2();

			flight.setFlightTicks(0);
			flight.setTargetTicks(FlightUtil.getFlightDuration(
					travel.position(), travel.destination())
			);

			return false;
		});
	}

	@Override
	public void onLoaded() {
		handbrake.of(this, HANDBRAKE);
		autopilot.of(this, AUTOPILOT);
	}

	private boolean isInFlight() {
		return this.tardis().travel2().getState() == TravelHandler.State.FLIGHT
				|| this.tardis().travel2().getState() == TravelHandler.State.MAT;
	}

	private boolean isFlightTicking() {
		return this.tardis().travel2().getState() == TravelHandler.State.FLIGHT && this.getTargetTicks() != 0;
	}

	public boolean hasFinishedFlight() {
		return (this.getFlightTicks() >= this.getTargetTicks() || this.getTargetTicks() == 0 || tardis.travel2().isCrashing()) &&
				!PropertiesHandler.getBool(tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
	}

	private void onFlightFinished() {
		this.setFlightTicks(0);
		this.setTargetTicks(0);

		this.tardis.getDesktop().playSoundAtEveryConsole(SoundEvents.BLOCK_BELL_RESONATE);

		if (this.shouldAutoLand())
			this.tardis().travel2().rematerialize();
	}

	private boolean shouldAutoLand() {
		return (this.autopilot.get()
				|| !TardisUtil.isInteriorNotEmpty(this.tardis()))
				&& !PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
		// todo im not too sure if this second check should exist, but its so funny ( ghost monument reference )
	}

	public void increaseFlightTime(int ticks) {
		this.setTargetTicks(this.getTargetTicks() + ticks);
	}

	public void decreaseFlightTime(int ticks) {
		this.setTargetTicks(this.getTargetTicks() - ticks);
	}

	public int getDurationAsPercentage() {
		if (this.getTargetTicks() == 0 || this.getFlightTicks() == 0) {
			if (this.tardis().travel2().getState() == TravelHandler.State.DEMAT)
				return 0;

			return 100;
		}

		return FlightUtil.getDurationAsPercentage(this.getFlightTicks(), this.getTargetTicks());
	}

	public void recalculate() {
		this.setTargetTicks(FlightUtil.getFlightDuration(tardis.travel2().position(), tardis.travel2().destination()));
		this.setFlightTicks(this.isInFlight() ? MathHelper.clamp(this.getFlightTicks(), 0, this.getTargetTicks()) : 0);
	}

	public int getFlightTicks() {
		if (!this.tardis.properties().getData().containsKey(FLIGHT_TICKS_KEY))
			this.setFlightTicks(0);

		return PropertiesHandler.getInt(this.tardis().properties(), FLIGHT_TICKS_KEY);
	}

	public void setFlightTicks(int ticks) {
		PropertiesHandler.set(this.tardis(), FLIGHT_TICKS_KEY, ticks);
	}

	public int getTargetTicks() {
		if (!this.tardis().properties().getData().containsKey(TARGET_TICKS_KEY))
			this.setTargetTicks(0);

		return PropertiesHandler.getInt(this.tardis().properties(), TARGET_TICKS_KEY);
	}

	private void setTargetTicks(int ticks) {
		PropertiesHandler.set(this.tardis(), TARGET_TICKS_KEY, ticks);
	}

	public void handbrake(boolean value) {
		if (this.tardis.travel2().getState() == TravelHandlerBase.State.DEMAT && value)
			this.tardis.travel2().cancelDemat();

		handbrake.set(value);
	}

	public boolean handbrake() {
		return handbrake.get();
	}

	public BoolValue autopilot() {
		return autopilot;
	}

	@Override
	public void tick(MinecraftServer server) {
		Tardis tardis = this.tardis();

		TardisCrashData crash = tardis.crash();
		TravelHandlerV2 travel = tardis.travel2();

		if (crash.getState() != TardisCrashData.State.NORMAL)
			crash.addRepairTicks(2 * travel.speed().get());

		if ((this.getTargetTicks() > 0 || this.getFlightTicks() > 0) && travel.getState() == TravelHandler.State.LANDED)
			this.recalculate();

		this.triggerSequencingDuringFlight(tardis);

		if (this.isInFlight() && !travel.isCrashing() && this.getTargetTicks() == 0 && this.getFlightTicks() < this.getTargetTicks())
			this.recalculate();

		if (this.isFlightTicking()) {
			if (this.hasFinishedFlight())
				this.onFlightFinished();

			this.setFlightTicks(this.getFlightTicks() + (Math.max(travel.speed().get() / 2, 1)));
		}

		if (!PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT)
				&& this.isInFlight() && this.hasFinishedFlight() && !TardisUtil.isInteriorNotEmpty(tardis))
			travel.rematerialize();
	}

	public void triggerSequencingDuringFlight(Tardis tardis) {
		SequenceHandler sequences = tardis.sequence();
		TravelHandlerV2 travel = tardis.travel2();

		if (!this.autopilot.get()
				&& this.getDurationAsPercentage() < 100
				&& travel.getState() == TravelHandler.State.FLIGHT
				&& !travel.position().equals(travel.destination())
				&& !sequences.hasActiveSequence()
				&& FlightUtil.getFlightDuration(travel.position(), tardis.travel2().destination()) > 100
				&& random.nextBetween(0, 460 / (travel.speed().get() == 0 ? 1 : travel.speed().get())) == 7) {
			sequences.triggerRandomSequence(true);
		}
	}
}
