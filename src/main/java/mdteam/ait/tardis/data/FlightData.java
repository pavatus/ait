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
    private int flightTicks = 0; // todo, this shouldnt reaally be synced to client or be saved in PropertiesHolder because it'll spam packets when in flight.
    private int targetTicks = 0;

    public FlightData(Tardis tardiz) {
        super(tardiz, "flight");
        if(getTardis().isEmpty()) return;

        // todo this doesnt seem to work.
        TardisEvents.LANDED.register((tardis -> {
            if (this.getTardis().isEmpty()) return;
            if (!tardis.equals(this.getTardis().get())) return;

            flightTicks = 0;
            targetTicks = 0;
        }));
        TardisEvents.DEMAT.register((tardis -> {
            if (this.getTardis().isEmpty()) return false;
            if (!tardis.equals(this.getTardis().get())) return false;

            flightTicks = 0;
            targetTicks = FlightUtil.getFlightDuration(tardis.getTravel().getPosition(), tardis.getTravel().getDestination());

            return false;
        }));
    }

    private boolean isInFlight() {
        if(getTardis().isEmpty()) return false;
        return this.getTardis().get().getTravel().getState().equals(TardisTravel.State.FLIGHT) || this.getTardis().get().getTravel().getState().equals(TardisTravel.State.MAT);
    }

    private boolean isFlightTicking() {
        if(getTardis().isEmpty()) return false;
        return this.getTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT && this.targetTicks != 0;
    }

    public boolean hasFinishedFlight() {
        if(getTardis().isEmpty()) return false;
        return flightTicks >= targetTicks || targetTicks == 0 || getTardis().get().getTravel().isCrashing();
    }
    private void onFlightFinished() {
        if(getTardis().isEmpty()) return;
        this.flightTicks = 0;
        this.targetTicks = 0;
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
        targetTicks = targetTicks + ticks;
    }

    public int getDurationAsPercentage() {
        if(getTardis().isEmpty()) return 0;
        if (this.targetTicks == 0 || this.flightTicks == 0) {
            if (this.getTardis().get().getTravel().getState() == TardisTravel.State.DEMAT) return 0;
            // if (this.tardis().getTravel().getState() == TardisTravel.State.MAT) return 100;
            return 100;
        }

        return FlightUtil.getDurationAsPercentage(this.flightTicks, this.targetTicks);
    }

    public void recalculate() {
        if(getTardis().isEmpty()) return;
        this.targetTicks = FlightUtil.getFlightDuration(getTardis().get().position(), getTardis().get().destination());
        this.flightTicks = this.isInFlight() ? MathHelper.clamp(this.flightTicks, 0, this.targetTicks) : 0;
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if(getTardis().isEmpty()) return;

        if ((this.targetTicks > 0 || this.flightTicks > 0) && this.getTardis().get().getTravel().getState() == TardisTravel.State.LANDED) {
            this.recalculate();
        }

        if (this.isInFlight() && !this.getTardis().get().getTravel().isCrashing() && !(this.flightTicks >= this.targetTicks) && this.targetTicks == 0) {
            this.recalculate();
        }

        if (this.isFlightTicking()) {
            if (this.hasFinishedFlight()) {
                this.onFlightFinished();
            }

            this.flightTicks = this.flightTicks + this.getTardis().get().getTravel().getSpeed();
        }
    }
}
