package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.TardisTravel;
import the.mdteam.ait.wrapper.server.ServerTardis;

public class PulsatingAnimation extends ExteriorAnimation{
    private int pulses = 0;
    private int PULSE_LENGTH = 20;
    private float frequency, intensity;

    public PulsatingAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        if (exterior.getTardis() == null)
            return;

        TardisTravel.State state = exterior.getTardis().getTravel().getState();

        if (state == TardisTravel.State.DEMAT) {
            this.setAlpha(1f - getPulseAlpha());
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            timeLeft--;

            if (timeLeft < maxTime)
                this.setAlpha(getPulseAlpha());
            else
                this.setAlpha(0f);

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.LANDED/* && alpha != 1f*/) {
            this.setAlpha(1f);
        }
    }

    public float getPulseAlpha() {
        if (timeLeft != maxTime && timeLeft % PULSE_LENGTH == 0)
            pulses++;

        return (float) ((float) (pulses / Math.floor((double) maxTime / PULSE_LENGTH)) + (Math.cos(timeLeft * frequency) * intensity)); // @TODO find alternative math or ask cwaig if we're allowed to use this, loqor says "its just math" but im still saying this just in case.
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
            timeLeft = 240;
            maxTime = timeLeft;
            frequency = 0.1f;
            intensity = 0.3f;
        } else if (state == TardisTravel.State.MAT){
            alpha = 0f;
            timeLeft = 460;
            maxTime = 240;
            frequency = 0.2f;
            intensity = 0.4f;
        } else if(state == TardisTravel.State.LANDED) {
            alpha = 1f;
            timeLeft = 0;
            maxTime = timeLeft;
        }

        pulses = 0;
    }
}
