package loqor.ait.tardis.data.travel;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.TardisCrashData;
import loqor.ait.tardis.data.TravelHandler;
import loqor.ait.tardis.data.TravelHandlerV2;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.data.properties.v2.integer.IntProperty;
import loqor.ait.tardis.data.properties.v2.integer.IntValue;
import loqor.ait.tardis.util.FlightUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public abstract class ProgressiveTravelHandler extends TravelHandlerBase implements TardisTickable {

    private static final Random random = Random.create();

    private static final IntProperty FLIGHT_TICKS = new IntProperty("flight_ticks");
    private static final IntProperty TARGET_TICKS = new IntProperty("target_ticks");

    private static final BoolProperty HANDBRAKE = new BoolProperty("handbrake", true);
    private static final BoolProperty AUTOPILOT = new BoolProperty("autopilot", false);

    private final IntValue flightTicks = FLIGHT_TICKS.create(this);
    private final IntValue targetTicks = TARGET_TICKS.create(this);

    protected final BoolValue handbrake = HANDBRAKE.create(this);
    protected final BoolValue autopilot = AUTOPILOT.create(this);

    public ProgressiveTravelHandler(Id id) {
        super(id);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        flightTicks.of(this, FLIGHT_TICKS);
        targetTicks.of(this, TARGET_TICKS);

        handbrake.of(this, HANDBRAKE);
        autopilot.of(this, AUTOPILOT);
    }

    private boolean isInFlight() {
        return this.getState() == TravelHandler.State.FLIGHT || this.getState() == TravelHandler.State.MAT;
    }

    private boolean isFlightTicking() {
        return this.tardis().travel2().getState() == TravelHandler.State.FLIGHT && this.getTargetTicks() != 0;
    }

    public boolean hasFinishedFlight() {
        return (this.getFlightTicks() >= this.getTargetTicks() || this.getTargetTicks() == 0 || tardis.travel2().isCrashing()) &&
                !PropertiesHandler.getBool(tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
    }

    // TODO inline
    private void onFlightFinished() {
        this.flightTicks.set(0);
        this.targetTicks.set(0);

        this.tardis.getDesktop().playSoundAtEveryConsole(SoundEvents.BLOCK_BELL_RESONATE);

        if (this.autopilot.get() && !PropertiesHandler.getBool(this.tardis.properties(), PropertiesHandler.IS_IN_REAL_FLIGHT))
            this.tardis().travel2().rematerialize();
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
        this.setTargetTicks(FlightUtil.getFlightDuration(this.position(), this.destination()));
        this.setFlightTicks(this.isInFlight() ? MathHelper.clamp(this.getFlightTicks(), 0, this.getTargetTicks()) : 0);
    }

    protected void startFlight() {
        this.setFlightTicks(0);
        this.setTargetTicks(FlightUtil.getFlightDuration(
                this.position(), this.destination())
        );
    }

    protected void resetFlight() {
        this.setFlightTicks(0);
        this.setTargetTicks(0);
    }

    public int getFlightTicks() {
        return this.flightTicks.get();
    }

    public void setFlightTicks(int ticks) {
        this.flightTicks.set(ticks);
    }

    public int getTargetTicks() {
        return this.targetTicks.get();
    }

    protected void setTargetTicks(int ticks) {
        this.targetTicks.set(ticks);
    }

    public void handbrake(boolean value) {
        if (this.getState() == TravelHandlerBase.State.DEMAT && value)
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
    }

    public void triggerSequencingDuringFlight(Tardis tardis) {
        SequenceHandler sequences = tardis.sequence();

        if (!this.autopilot.get()
                && this.getDurationAsPercentage() < 100
                && this.getState() == TravelHandler.State.FLIGHT
                && !this.position().equals(this.destination())
                && !sequences.hasActiveSequence()
                && FlightUtil.getFlightDuration(this.position(), this.destination()) > 100
                && random.nextBetween(0, 460 / (this.speed().get() == 0 ? 1 : this.speed().get())) == 7) {
            sequences.triggerRandomSequence(true);
        }
    }
}
