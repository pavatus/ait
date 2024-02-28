package mdteam.ait.client.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

// Referencing how music which loops is done but in our own way
@Environment(EnvType.CLIENT)
public abstract class LoopingSound extends MovingSoundInstance {
	public LoopingSound(SoundEvent soundEvent, SoundCategory soundCategory) {
		super(soundEvent, soundCategory, Random.create());
		this.repeat = true;
	}

	@Override
	public boolean isRepeatable() {
		return true;
	}

	@Override
	public void tick() {
	}
}
