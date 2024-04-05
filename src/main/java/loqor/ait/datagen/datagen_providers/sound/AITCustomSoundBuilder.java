package loqor.ait.datagen.datagen_providers.sound;

import net.minecraft.sound.SoundEvent;

@FunctionalInterface
public interface AITCustomSoundBuilder {
	void add(String soundName, SoundEvent[] soundEvents);

	default void add(String soundName, SoundEvent soundEvent) {
		add(soundName, new SoundEvent[]{soundEvent});
	}
}
