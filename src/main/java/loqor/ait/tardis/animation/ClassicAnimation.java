package loqor.ait.tardis.animation;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.tardis.TardisTravel;

public class ClassicAnimation extends ExteriorAnimation {

	public ClassicAnimation(ExteriorBlockEntity exterior) {
		super(exterior);
	}

	@Override
	public void tick() {
		if (exterior.tardis().isEmpty())
			return;

		TardisTravel.State state = exterior.tardis().get().travel().getState();

		if (this.timeLeft < 0)
			this.setupAnimation(exterior.tardis().get().travel().getState()); // fixme is a jank fix for the timeLeft going negative on client

		if (state == TardisTravel.State.DEMAT) {
			timeLeft--;
			this.setAlpha(getFadingAlpha());

			runAlphaChecks(state);
		} else if (state == TardisTravel.State.MAT) {
			timeLeft++;

			if (timeLeft > 680) {
				this.setAlpha(((float) timeLeft - 680) / (860 - 620));
			} else {
				this.setAlpha(0f);
			}

			runAlphaChecks(state);
		} else if (state == TardisTravel.State.LANDED/* && alpha != 1f*/) {
			this.setAlpha(1f);
		}
	}

	public float getFadingAlpha() {
		return (float) (timeLeft) / (maxTime);
	}

	@Override
	public void setupAnimation(TardisTravel.State state) {
		if (exterior.tardis().isEmpty() || exterior.tardis().get().getExterior().getCategory() == null) {
			AITMod.LOGGER.error("Tardis for exterior " + exterior + " was null! Panic!!!!");
			alpha = 0f; // just make me vanish.
			return;
		}

		MatSound sound = exterior.tardis().get().getExterior().getVariant().getSound(state);
		this.tellClientsToSetup(state);

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
