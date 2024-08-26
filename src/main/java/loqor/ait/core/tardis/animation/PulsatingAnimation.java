package loqor.ait.core.tardis.animation;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;

public class PulsatingAnimation extends ExteriorAnimation {

    private static final int PULSE_LENGTH = 20;

    private int pulses = 0;
    private float frequency, intensity;

    public PulsatingAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick(Tardis tardis) {
        TravelHandler travel = tardis.travel();
        TravelHandlerBase.State state = travel.getState();

        if (this.timeLeft < 0)
            this.setupAnimation(travel.getState()); // fixme is a jank fix for the timeLeft going negative on
        // client

        if (state == TravelHandlerBase.State.DEMAT)
            this.setAlpha(1f - this.getPulseAlpha());

        if (state == TravelHandlerBase.State.MAT) {
            if (timeLeft < startTime)
                this.setAlpha(this.getPulseAlpha());
            else
                this.alpha = 0f;
        }

        this.timeLeft--;
    }

    @Override
    public boolean reset() {
        if (!super.reset())
            return false;

        this.pulses = 0;
        return true;
    }

    public float getPulseAlpha() {
        if (timeLeft != maxTime && timeLeft % PULSE_LENGTH == 0)
            pulses++;

        return (float) ((float) (pulses / Math.floor((double) maxTime / PULSE_LENGTH))
                + (Math.cos(timeLeft * frequency) * intensity));
    }

    @Override
    public boolean setupAnimation(TravelHandlerBase.State state) {
        if (!super.setupAnimation(state))
            return false;

        MatSound sound = state.effect();

        this.frequency = sound.frequency();
        this.intensity = sound.intensity();

        this.pulses = 0;
        return true;
    }
}
