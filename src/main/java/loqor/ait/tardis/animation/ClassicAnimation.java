package loqor.ait.tardis.animation;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TravelHandler;

public class ClassicAnimation extends ExteriorAnimation {

	public ClassicAnimation(ExteriorBlockEntity exterior) {
		super(exterior);
	}

	@Override
	public void tick() {
		if (exterior.tardis().isEmpty())
			return;

		Tardis tardis = exterior.tardis().get();
		TravelHandler travel = tardis.travel();

		TravelHandler.State state = travel.getState();

		if (this.timeLeft < 0)
			this.setupAnimation(state); // fixme is a jank fix for the timeLeft going negative on client

		if (state == TravelHandler.State.DEMAT) {
			this.timeLeft--;
			this.setAlpha(getFadingAlpha());

			if (this.alpha <= 0f)
				travel.fly();

			return;
		}

		if (state == TravelHandler.State.MAT) {
			this.timeLeft++;

			if (this.timeLeft > 680) {
				this.setAlpha(((float) this.timeLeft - 680) / (860 - 620));
			} else {
				this.alpha = 0f;
			}

			if (alpha >= 1f)
				tardis.travel().land(this.exterior);

			return;
		}

		if (state == TravelHandler.State.LANDED) {
			this.alpha = 1f;
		}
	}

	public float getFadingAlpha() {
		return (float) (timeLeft) / (maxTime);
	}

	@Override
	public void setupAnimation(TravelHandler.State state) {
		if (exterior.tardis().isEmpty()) {
			AITMod.LOGGER.error("Tardis for exterior {} is null!", exterior);

			this.alpha = 0f; // just make me vanish.
			return;
		}

		Tardis tardis = exterior.tardis().get();

		if (tardis.getExterior().getCategory() == null) {
			AITMod.LOGGER.error("Exterior category {} was null!", exterior);

			this.alpha = 0f; // just make me vanish.
			return;
		}

		MatSound sound = tardis.getExterior().getVariant().getSound(state);
		this.tellClientsToSetup(state);

		timeLeft = sound.timeLeft();
		maxTime = sound.maxTime();
		startTime = sound.startTime();

		this.alpha = switch (state) {
			case DEMAT, LANDED -> 1f;
            case MAT -> 0f;

            default -> throw new IllegalStateException("Unreachable.");
        };
	}
}
