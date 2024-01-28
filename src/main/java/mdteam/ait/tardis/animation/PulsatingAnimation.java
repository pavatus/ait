package mdteam.ait.tardis.animation;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.tardis.TardisTravel;

public class PulsatingAnimation extends ExteriorAnimation {
    private int pulses = 0;
    private int PULSE_LENGTH = 20;
    private float frequency, intensity;

    public PulsatingAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        if (exterior.getTardis().isEmpty())
            return;

        TardisTravel.State state = exterior.getTardis().get().getTravel().getState();


        if (this.timeLeft < 0)
            this.setupAnimation(exterior.getTardis().get().getTravel().getState()); // fixme is a jank fix for the timeLeft going negative on client

        if (state == TardisTravel.State.DEMAT) {
            this.setAlpha(1f - getPulseAlpha());
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            timeLeft--;

            if (timeLeft < startTime)
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
        if (exterior.getTardis().isEmpty() || exterior.getTardis().get().getExterior().getType() == null) {
            AITMod.LOGGER.error("Tardis for exterior " + exterior + " was null! Panic!!!!");
            alpha = 0f; // just make me vanish.
            return;
        }

        if (exterior.hasWorld() && exterior.getWorld().isClient()) {
            // todo - the Render Thread for some reason has the wrong state? / it overrides it. This kind of thing is happening quite frequently, and im not too sure what the cause is.
            System.out.println(exterior.getTardis().get().getTravel());
            System.out.println(exterior.getTardis().get());
            System.out.println(state);
        }

        MatSound sound = exterior.getTardis().get().getExterior().getVariant().getSound(state);

        this.tellClientsToSetup(state);

        timeLeft = sound.timeLeft();
        maxTime = sound.maxTime();
        frequency = sound.frequency();
        intensity = sound.intensity();
        startTime = sound.startTime();

        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
        } else if (state == TardisTravel.State.MAT) {
            alpha = 0f;
        } else if (state == TardisTravel.State.LANDED) {
            alpha = 1f;
        }

        pulses = 0;
    }
}
