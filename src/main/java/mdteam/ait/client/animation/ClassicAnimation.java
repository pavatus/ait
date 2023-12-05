package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.tardis.TardisTravel;

public class ClassicAnimation extends ExteriorAnimation {
    public ClassicAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        if (exterior.getTardis() == null)
            return;

        TardisTravel.State state = exterior.getTardis().getTravel().getState();

        // System.out.println(state);

        if (state == TardisTravel.State.DEMAT) {
            alpha = (float) timeLeft / (maxTime);
            timeLeft--;

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            if (timeLeft < startTime) {
                // System.out.println(alpha + alphaChangeAmount);
                this.setAlpha(1f - ((float) timeLeft / (startTime))); // fixme takes too long
            }
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
        MatSound sound = exterior.getTardis().getExterior().getType().getSound(state);

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
