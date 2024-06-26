package loqor.ait.tardis.animation;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TravelHandler;
import loqor.ait.tardis.data.TravelHandlerV2;

public class PulsatingAnimation extends ExteriorAnimation {
	private static final int PULSE_LENGTH = 20;

	private int pulses = 0;
	private float frequency, intensity;

	public PulsatingAnimation(ExteriorBlockEntity exterior) {
		super(exterior);
	}

	@Override
	public void tick() {
		if (exterior.tardis().isEmpty())
			return;

		Tardis tardis = exterior.tardis().get();
		TravelHandlerV2 travel = tardis.travel2();

		TravelHandler.State state = travel.getState();

		if (this.timeLeft < 0)
			this.setupAnimation(travel.getState()); // fixme is a jank fix for the timeLeft going negative on client

		if (state == TravelHandler.State.DEMAT) {
			this.setAlpha(1f - this.getPulseAlpha());
			this.timeLeft--;

			//if (this.alpha <= 0f)
			//	travel.finishDemat();

			return;
		}

		if (state == TravelHandler.State.MAT) {
			this.timeLeft--;

			if (timeLeft < startTime)
				this.setAlpha(this.getPulseAlpha());
			else this.alpha = 0f;

			//if (alpha >= 1f)
			//	travel.finishLanding(this.exterior);

			return;
		}

		if (state == TravelHandler.State.LANDED)
			this.alpha = 1f;
	}

	public float getPulseAlpha() {
		if (timeLeft != maxTime && timeLeft % PULSE_LENGTH == 0)
			pulses++;

		return (float) ((float) (pulses / Math.floor((double) maxTime / PULSE_LENGTH)) + (Math.cos(timeLeft * frequency) * intensity)); // @TODO find alternative math or ask cwaig if we're allowed to use this, loqor says "its just math" but im still saying this just in case.
	}

	@Override
	public void setupAnimation(TravelHandler.State state) {
		if (exterior.tardis().isEmpty()) {
			AITMod.LOGGER.error("Tardis for exterior " + exterior + " was null! Panic!!!!");
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

		this.timeLeft = sound.timeLeft();
		this.maxTime = sound.maxTime();
		this.frequency = sound.frequency();
		this.intensity = sound.intensity();
		this.startTime = sound.startTime();

		this.alpha = switch (state) {
			case DEMAT, LANDED -> 1f;
			case MAT -> 0f;

			default -> throw new IllegalStateException("Unreachable.");
		};

		this.pulses = 0;
	}
}
