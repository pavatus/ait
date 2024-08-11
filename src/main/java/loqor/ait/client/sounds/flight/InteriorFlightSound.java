package loqor.ait.client.sounds.flight;

import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.Random;

public class InteriorFlightSound extends PlayerFollowingLoopingSound {
	private static final Random rnd = new Random();
	private static final int PITCH_CHANGE_TICK = 80;
	private int ticks = 0;

	public InteriorFlightSound(SoundEvent soundEvent, SoundCategory soundCategory, float volume) {
		super(soundEvent, soundCategory, volume);
	}

	@Override
	public void tick() {
		super.tick();
		this.ticks++;

		if (this.ticks >= PITCH_CHANGE_TICK) {
			this.pitch = getRandomPitch();
			this.ticks = 0;
		}

		this.volume = (float) ((1f - (ClientTardisUtil.distanceFromConsole() / ClientFlightHandler.MAX_DISTANCE))); // laag?
	}

	private static float getRandomPitch() {
		ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

		if (tardis == null)
			return 1f;

		int speed = tardis.travel().speed();

		if (ClientSoundManager.getFlight().hasThrottleAndHandbrakeDown(tardis)) {
			// todo i hate switch
			return switch (speed) {
				default -> 1.0f;
				case 1 -> 0.5f;
				case 2 -> 0.55f;
				case 3 -> 0.6f;
			};
		}

		return switch (speed) {
			default -> 1.0f;
			case 1 -> rnd.nextFloat(0.9f, 0.95f);
			case 2 -> rnd.nextFloat(0.95f, 1.0f);
			case 3 -> rnd.nextFloat(1.0f, 1.25f);
		};
	}
}
