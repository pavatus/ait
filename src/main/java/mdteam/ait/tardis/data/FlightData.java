package mdteam.ait.tardis.data;

import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class FlightData extends TardisLink {
    private static final String FLIGHT_TICKS_KEY = "flight_ticks";
    private static final String TARGET_TICKS_KEY = "target_ticks";

    public FlightData(Tardis tardiz) {
        super(tardiz, "flight");
        if(getTardis().isEmpty()) return;

        // todo this doesnt seem to work.
        TardisEvents.LANDED.register((tardis -> {
            if (this.getTardis().isEmpty()) return;
            if (!tardis.equals(this.getTardis().get())) return;

            this.setFlightTicks(0);
            this.setTargetTicks(0);
        }));
        TardisEvents.DEMAT.register((tardis -> {
            if (this.getTardis().isEmpty()) return false;
            if (!tardis.equals(this.getTardis().get())) return false;

            this.setFlightTicks(0);
            this.setTargetTicks(FlightUtil.getFlightDuration(tardis.getTravel().getPosition(), tardis.getTravel().getDestination()));

            return false;
        }));
    }

    private boolean isInFlight() {
        if(getTardis().isEmpty()) return false;
        return this.getTardis().get().getTravel().getState().equals(TardisTravel.State.FLIGHT) || this.getTardis().get().getTravel().getState().equals(TardisTravel.State.MAT);
    }

    private boolean isFlightTicking() {
        if(getTardis().isEmpty()) return false;
        return this.getTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT && this.getTargetTicks() != 0;
    }

    public boolean hasFinishedFlight() {
        if(getTardis().isEmpty()) return false;
        return this.getFlightTicks() >= this.getTargetTicks() || this.getTargetTicks() == 0 || getTardis().get().getTravel().isCrashing();
    }
    private void onFlightFinished() {
        if(getTardis().isEmpty()) return;
        this.setFlightTicks(0);
        this.setTargetTicks(0);
        FlightUtil.playSoundAtConsole(getTardis().get(), SoundEvents.BLOCK_BEACON_POWER_SELECT); // temp sound

        if (shouldAutoLand()) {
            this.getTardis().get().getTravel().materialise();
        }
    }
    private boolean shouldAutoLand() {
        if(getTardis().isEmpty()) return false;
        return PropertiesHandler.willAutoPilot(getTardis().get().getHandlers().getProperties()) || !TardisUtil.isInteriorNotEmpty(this.getTardis().get()); // todo im not too sure if this second check should exist, but its so funny ( ghost monument reference )
    }

    public void increaseFlightTime(int ticks) {
        this.setTargetTicks(this.getTargetTicks() + ticks);
    }

    public int getDurationAsPercentage() {
        if(getTardis().isEmpty()) return 0;
        if (this.getTargetTicks() == 0 || this.getFlightTicks() == 0) {
            if (this.getTardis().get().getTravel().getState() == TardisTravel.State.DEMAT) return 0;
            // if (this.tardis().getTravel().getState() == TardisTravel.State.MAT) return 100;
            return 100;
        }

        return FlightUtil.getDurationAsPercentage(this.getFlightTicks(), this.getTargetTicks());
    }

    public void recalculate() {
        if(getTardis().isEmpty()) return;
        this.setTargetTicks(FlightUtil.getFlightDuration(getTardis().get().position(), getTardis().get().destination()));
        this.setFlightTicks(this.isInFlight() ? MathHelper.clamp(this.getFlightTicks(), 0, this.getTargetTicks()) : 0);
    }

    public int getFlightTicks() {
        if (this.getTardis().isEmpty()) return 0;

        if (!this.getTardis().get().getHandlers().getProperties().getData().containsKey(FLIGHT_TICKS_KEY)) {
            this.setFlightTicks(0);
        }

        return PropertiesHandler.getInt(this.getTardis().get().getHandlers().getProperties(), FLIGHT_TICKS_KEY);
    }
    private void setFlightTicks(int ticks) {
       if (this.getTardis().isEmpty()) return;

       PropertiesHandler.set(this.getTardis().get(), FLIGHT_TICKS_KEY, ticks);
    }

    public int getTargetTicks() {
        if (this.getTardis().isEmpty()) return 0;

        if (!this.getTardis().get().getHandlers().getProperties().getData().containsKey(TARGET_TICKS_KEY)) {
            this.setTargetTicks(0);
        }

        return PropertiesHandler.getInt(this.getTardis().get().getHandlers().getProperties(), TARGET_TICKS_KEY);
    }
    private void setTargetTicks(int ticks) {
        if (this.getTardis().isEmpty()) return;

        PropertiesHandler.set(this.getTardis().get(), TARGET_TICKS_KEY, ticks);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if(getTardis().isEmpty()) return;

        if ((this.getTargetTicks() > 0 || this.getFlightTicks() > 0) && this.getTardis().get().getTravel().getState() == TardisTravel.State.LANDED) {
            this.recalculate();
        }

        if (this.isInFlight() && !this.getTardis().get().getTravel().isCrashing() && !(this.getFlightTicks() >= this.getTargetTicks()) && this.getTargetTicks() == 0) {
            this.recalculate();
        }

        if (this.isFlightTicking()) {
            if (this.hasFinishedFlight()) {
                this.onFlightFinished();
            }

            this.setFlightTicks(this.getFlightTicks() + this.getTardis().get().getTravel().getSpeed());
        }
    }
}
