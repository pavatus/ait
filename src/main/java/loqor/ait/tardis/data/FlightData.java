package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisLink;
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
import net.minecraft.world.World;

public class FlightData extends TardisLink {
	private static final String FLIGHT_TICKS_KEY = "flight_ticks";
	private static final String TARGET_TICKS_KEY = "target_ticks";
	private static final Random random = Random.create();

	public FlightData() {
		super(Id.FLIGHT);
	}

	static {
		TardisEvents.LANDED.register(tardis -> {
			FlightData flight = tardis.flight();

			flight.setFlightTicks(0);
			flight.setTargetTicks(0);

			AbsoluteBlockPos.Directed pos = flight.getExteriorPos();
			World world = pos.getWorld();

			if(world != null) {
				world.setBlockState(pos, pos.getBlockState().with(ExteriorBlock.LEVEL_9, 9), 3);
			}
		});

		TardisEvents.DEMAT.register(tardis -> {
			FlightData flight = tardis.flight();
			TardisTravel travel = tardis.travel();

			flight.setFlightTicks(0);
			flight.setTargetTicks(FlightUtil.getFlightDuration(
					travel.getPosition(), travel.getDestination())
			);

			return false;
		});
	}

	private boolean isInFlight() {
		return this.tardis().travel().getState().equals(TardisTravel.State.FLIGHT) || this.tardis().travel().getState().equals(TardisTravel.State.MAT);
	}

	private boolean isFlightTicking() {
		return this.tardis().travel().getState() == TardisTravel.State.FLIGHT && this.getTargetTicks() != 0;
	}

	public boolean hasFinishedFlight() {
		return (this.getFlightTicks() >= this.getTargetTicks() || this.getTargetTicks() == 0 ||
				tardis().travel().isCrashing()) &&
				!PropertiesHandler.getBool(tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
	}

	private void onFlightFinished() {
		this.setFlightTicks(0);
		this.setTargetTicks(0);

		FlightUtil.playSoundAtConsole(this.tardis(), SoundEvents.BLOCK_BELL_RESONATE); // temp sound

		if (shouldAutoLand()) {
			this.tardis().travel().materialise();
		}
	}

	private boolean shouldAutoLand() {
		return (this.tardis().travel().autoLand().get()
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
			if (this.tardis().travel().getState() == TardisTravel.State.DEMAT)
				return 0;

			return 100;
		}

		return FlightUtil.getDurationAsPercentage(this.getFlightTicks(), this.getTargetTicks());
	}

	public void recalculate() {
		this.setTargetTicks(FlightUtil.getFlightDuration(tardis().position(), tardis().destination()));
		this.setFlightTicks(this.isInFlight() ? MathHelper.clamp(this.getFlightTicks(), 0, this.getTargetTicks()) : 0);
	}

	public int getFlightTicks() {
		if (!this.tardis().properties().getData().containsKey(FLIGHT_TICKS_KEY)) {
			this.setFlightTicks(0);
		}

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

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		ServerTardis tardis = (ServerTardis) this.tardis();

		TardisCrashData crash = tardis.crash();
		TardisTravel travel = tardis.travel();

		if (crash.getState() != TardisCrashData.State.NORMAL)
			crash.addRepairTicks(2 * travel.speed().get());

		if ((this.getTargetTicks() > 0 || this.getFlightTicks() > 0) && travel.getState() == TardisTravel.State.LANDED)
			this.recalculate();

		triggerSequencingDuringFlight(tardis);

		if (this.isInFlight() && !travel.isCrashing() && !(this.getFlightTicks() >= this.getTargetTicks()) && this.getTargetTicks() == 0) {
			this.recalculate();
		}

		if (this.isFlightTicking()) {
			if (this.hasFinishedFlight()) {
				this.onFlightFinished();
			}

			this.setFlightTicks(this.getFlightTicks() + (Math.max(travel.speed().get() / 2, 1)));
		}

		if (!PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT)
				&& this.isInFlight() && this.hasFinishedFlight() && !TardisUtil.isInteriorNotEmpty(tardis))
			travel.materialise();
	}

	public void triggerSequencingDuringFlight(Tardis tardis) {
		SequenceHandler sequences = tardis.sequence();
		TardisTravel travel = tardis.travel();

		if (!tardis.travel().autoLand().get()
				&& this.getDurationAsPercentage() < 100
				&& travel.inFlight() && tardis.position() != tardis.destination() && !sequences.hasActiveSequence()) {
			if (FlightUtil.getFlightDuration(tardis.position(),
					tardis.destination()) > FlightUtil.convertSecondsToTicks(5)) {
				int rand = random.nextBetween(0, 460 / (tardis.travel().speed().get() == 0 ? 1 : tardis.travel().speed().get()));
				if (rand == 7) {
					sequences.triggerRandomSequence(true);
				}
			}
		}
	}
}
