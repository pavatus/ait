package loqor.ait.tardis.animation;

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
	public void tick(Tardis tardis) {
		TravelHandlerV2 travel = tardis.travel2();
		TravelHandler.State state = travel.getState();

		if (this.timeLeft < 0)
			this.setupAnimation(travel.getState()); // fixme is a jank fix for the timeLeft going negative on client

		if (state == TravelHandler.State.DEMAT)
			this.setAlpha(1f - this.getPulseAlpha());

		if (state == TravelHandler.State.MAT) {
			if (timeLeft < startTime) this.setAlpha(this.getPulseAlpha());
			else this.alpha = 0f;
		}

		this.timeLeft--;
	}

	public float getPulseAlpha() {
		if (timeLeft != maxTime && timeLeft % PULSE_LENGTH == 0)
			pulses++;

		return (float) ((float) (pulses / Math.floor((double) maxTime / PULSE_LENGTH)) + (Math.cos(timeLeft * frequency) * intensity)); // @TODO find alternative math or ask cwaig if we're allowed to use this, loqor says "its just math" but im still saying this just in case.
	}

	@Override
	public boolean setupAnimation(TravelHandler.State state) {
		if (!super.setupAnimation(state))
			return false;

		MatSound sound = state.effect();

		this.frequency = sound.frequency();
		this.intensity = sound.intensity();
		return true;
	}
}
