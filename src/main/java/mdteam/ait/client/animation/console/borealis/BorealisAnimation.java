package mdteam.ait.client.animation.console.borealis;

import mdteam.ait.client.animation.console.ConsoleAnimation;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import the.mdteam.ait.TardisTravel;

public class BorealisAnimation extends ConsoleAnimation {

    public BorealisAnimation(ConsoleBlockEntity console) {
        super(console);
    }

    @Override
    public void tick() {
        if (console.getTardis() == null)
            return;

        TardisTravel.State state = console.getTardis().getTravel().getState();

        /*if (state == TardisTravel.State.DEMAT) {
            alpha -= alphaChangeAmount;
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            if (timeLeft < startTime)
                //this.setAlpha(alpha + alphaChangeAmount);
            else
                //this.setAlpha(0f);

            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.LANDED*//* && alpha != 1f*//*) {
            this.setAlpha(1f);
        }*/
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        MatSound sound = console.getConsole().getSound(state);

        timeLeft = sound.timeLeft();
        maxTime = sound.maxTime();
        startTime = sound.startTime();

        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
        } else if (state == TardisTravel.State.MAT) {
            alpha = 0f;
        } else if (state == TardisTravel.State.LANDED) {
            alpha = 1f;
        }
    }
}
