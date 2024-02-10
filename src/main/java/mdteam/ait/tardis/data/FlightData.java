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
        if(findTardis().isEmpty()) return;

        // todo this doesnt seem to work.
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
        if(findTardis().isEmpty()) return false;
        return this.findTardis().get().getTravel().getState().equals(TardisTravel.State.FLIGHT) || this.findTardis().get().getTravel().getState().equals(TardisTravel.State.MAT);
    }

    private boolean isFlightTicking() {
        if(findTardis().isEmpty()) return false;
        return this.findTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT && this.getTargetTicks() != 0;
    }

    public boolean hasFinishedFlight() {
        if(findTardis().isEmpty()) return false;
        return this.getFlightTicks() >= this.getTargetTicks() || this.getTargetTicks() == 0 || findTardis().get().getTravel().isCrashing();
    }
    private void onFlightFinished() {
        if(findTardis().isEmpty()) return;
        this.setFlightTicks(0);
        this.setTargetTicks(0);
        FlightUtil.playSoundAtConsole(findTardis().get(), SoundEvents.BLOCK_BEACON_POWER_SELECT); // temp sound

        if (shouldAutoLand()) {
            this.findTardis().get().getTravel().materialise();
        }
    }
    private boolean shouldAutoLand() {
        if(findTardis().isEmpty()) return false;
        return PropertiesHandler.willAutoPilot(findTardis().get().getHandlers().getProperties()) || !TardisUtil.isInteriorNotEmpty(this.findTardis().get()); // todo im not too sure if this second check should exist, but its so funny ( ghost monument reference )
    }

    public void increaseFlightTime(int ticks) {
        this.setTargetTicks(this.getTargetTicks() + ticks);
    }

    public int getDurationAsPercentage() {
        if(findTardis().isEmpty()) return 0;
        if (this.getTargetTicks() == 0 || this.getFlightTicks() == 0) {
            if (this.findTardis().get().getTravel().getState() == TardisTravel.State.DEMAT) return 0;
            // if (this.tardis().getTravel().getState() == TardisTravel.State.MAT) return 100;
            return 100;
        }

        return FlightUtil.getDurationAsPercentage(this.getFlightTicks(), this.getTargetTicks());
    }

    public void recalculate() {
        if(findTardis().isEmpty()) return;
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
        if(findTardis().isEmpty()) return;
        findTardis().get().getHandlers().getCrashData().addRepairTicks(2 * findTardis().get().getTravel().getSpeed());
        if ((this.getTargetTicks() > 0 || this.getFlightTicks() > 0) && this.findTardis().get().getTravel().getState() == TardisTravel.State.LANDED) {
            this.recalculate();
        }

        if (this.isInFlight() && !this.findTardis().get().getTravel().isCrashing() && !(this.getFlightTicks() >= this.getTargetTicks()) && this.getTargetTicks() == 0) {
            this.recalculate();
        }

        if (this.isFlightTicking()) {
            if (this.hasFinishedFlight()) {
                this.onFlightFinished();
            }

            this.setFlightTicks(this.getFlightTicks() + this.findTardis().get().getTravel().getSpeed());
        }
    }
}
