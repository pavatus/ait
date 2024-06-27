package loqor.ait.tardis.animation;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TravelHandler;

public class ClassicAnimation extends ExteriorAnimation {

	public ClassicAnimation(ExteriorBlockEntity exterior) {
		super(exterior);
	}

	@Override
	public void tick(Tardis tardis) {
		TravelHandler.State state = tardis.travel2().getState();

		if (this.timeLeft < 0)
			this.setupAnimation(tardis.travel2().getState()); // fixme is a jank fix for the timeLeft going negative on client

		if (state == TravelHandler.State.DEMAT) {
			timeLeft--;
			this.setAlpha(getFadingAlpha());
		} else if (state == TravelHandler.State.MAT) {
			timeLeft++;

			if (timeLeft > 680) {
				this.setAlpha(((float) timeLeft - 680) / (860 - 620));
			} else {
				this.setAlpha(0f);
			}
		} else if (state == TravelHandler.State.LANDED) {
			this.setAlpha(1f);
		}
	}

	public float getFadingAlpha() {
		return (float) (timeLeft) / (maxTime);
	}
}
