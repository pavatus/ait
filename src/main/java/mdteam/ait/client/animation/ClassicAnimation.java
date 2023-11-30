package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import the.mdteam.ait.TardisTravel;

public class ClassicAnimation extends ExteriorAnimation {

    public ClassicAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        if (exterior.getTardis() == null)
            return;

        TardisTravel.State state = exterior.getTardis().getTravel().getState();

        if (state == TardisTravel.State.DEMAT) {
            alpha -= alphaChangeAmount;
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            if (timeLeft < startTime)
                this.setAlpha(alpha + alphaChangeAmount);
            else
                this.setAlpha(0f);

            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.LANDED/* && alpha != 1f*/) {
            this.setAlpha(1f);
        }
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        MatSound sound = exterior.getExterior().getSound(state);

        timeLeft = sound.timeLeft();
        maxTime = sound.maxTime();
        startTime = sound.startTime();

        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
        } else if (state == TardisTravel.State.MAT){
            alpha = 0f;
        } else if (state == TardisTravel.State.LANDED) {
            alpha = 1f;
        }
    }
}
