package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import the.mdteam.ait.TardisTravel;

public class ClassicAnimation extends ExteriorAnimation {

    public ClassicAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        TardisTravel.State state = exterior.getTardis().getTravel().getState();

        if (state == TardisTravel.State.DEMAT) {
            alpha -= alphaChangeAmount;
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            alpha += alphaChangeAmount;
            timeLeft--;

            runAlphaChecks(state);
        } else if (!exterior.getWorld().isClient() && state == TardisTravel.State.LANDED && alpha != 1f) {
            this.setAlpha(1f);
        }
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
            timeLeft = 150;
        } else if (state == TardisTravel.State.MAT){
            alpha = 0f;
            timeLeft = 200;
        } else {
            alpha = 1f;
            timeLeft = 0;
        }

        maxTime = timeLeft;
    }
}
