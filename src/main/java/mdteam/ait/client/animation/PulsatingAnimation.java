package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import the.mdteam.ait.TardisTravel;

public class PulsatingAnimation extends ExteriorAnimation{
    private int pulses = 0;
    private int PULSE_LENGTH = 20;

    public PulsatingAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        if (exterior.getTardis() == null) {
            if (!exterior.getWorld().isClient())
                exterior.refindTardis();

            return;
        }

        TardisTravel.State state = exterior.getTardis().getTravel().getState();

        if (state == TardisTravel.State.DEMAT) {
            this.updateClient();
            this.setAlpha(1f - getPulseAlpha());
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            this.updateClient();
            timeLeft--;

            if (timeLeft < maxTime)
                this.setAlpha(getPulseAlpha());
            else
                this.setAlpha(0f);

            runAlphaChecks(state);
        } else if (!exterior.getWorld().isClient() && state == TardisTravel.State.LANDED && alpha != 1f)
            this.setAlpha(1f);


        this.updateClient();
    }

    public float getPulseAlpha() {
        if (timeLeft != maxTime && timeLeft % PULSE_LENGTH == 0)
            pulses++;

        return (float) ((float) (pulses / Math.floor(maxTime / PULSE_LENGTH)) + (Math.cos(timeLeft * 0.25) * 0.4f)); // @TODO find alternative math or ask cwaig if we're allowed to use this, loqor says "its just math" but im still saying this just in case.
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
            timeLeft = 240;
            maxTime = timeLeft;
        } else if (state == TardisTravel.State.MAT){
            alpha = 0f;
            timeLeft = 460;
            maxTime = 240;
        } else {
            alpha = 1f;
            timeLeft = 0;
            maxTime = timeLeft;
        }

        pulses = 0;

        this.updateClient();
    }
}
