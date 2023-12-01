package mdteam.ait.client.animation.console.temp;

import mdteam.ait.client.animation.console.ConsoleAnimation;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import the.mdteam.ait.TardisTravel;

public class TempAnimation extends ConsoleAnimation {
    private int pulses = 0;
    private int PULSE_LENGTH = 20;
    private float frequency, intensity;

    public TempAnimation(ConsoleBlockEntity console) {
        super(console);
    }

    @Override
    public void tick() {
        if (console.getTardis() == null)
            return;

        TardisTravel.State state = console.getTardis().getTravel().getState();

        /*if (state == TardisTravel.State.DEMAT) {
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
        } else if (state == TardisTravel.State.LANDED*//* && alpha != 1f*//*) {
            this.setAlpha(1f);
        }*/
    }

    public float getPulseAlpha() {
        if (timeLeft != maxTime && timeLeft % PULSE_LENGTH == 0)
            pulses++;

        return (float) ((float) (pulses / Math.floor((double) maxTime / PULSE_LENGTH)) + (Math.cos(timeLeft * frequency) * intensity)); // @TODO find alternative math or ask cwaig if we're allowed to use this, loqor says "its just math" but im still saying this just in case.
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        MatSound sound = console.getConsole().getSound(state);

        timeLeft = sound.timeLeft();
        maxTime = sound.maxTime();
        frequency = sound.frequency();
        intensity = sound.intensity();
        startTime = sound.startTime();

        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
        } else if (state == TardisTravel.State.MAT){
            alpha = 0f;
        } else if (state == TardisTravel.State.LANDED) {
            alpha = 1f;
        }

        pulses = 0;
    }
}
