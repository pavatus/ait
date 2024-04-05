package loqor.ait.core.sounds;

import net.minecraft.sound.SoundEvent;

// @TODO better variable names
public record MatSound(SoundEvent sound, int timeLeft, int maxTime, int startTime, float frequency, float intensity) {
}