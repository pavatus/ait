package loqor.ait.core.sounds;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.api.Identifiable;

// @TODO better variable names
public record MatSound(Identifier id, SoundEvent sound, int timeLeft, int maxTime, int startTime, int length, float frequency,
        float intensity) implements Identifiable {
    public static final Codec<MatSound> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Identifier.CODEC.fieldOf("id").forGetter(MatSound::id),
                    SoundEvent.CODEC.fieldOf("sound").forGetter(MatSound::sound),
                    Codec.INT.fieldOf("timeLeft").forGetter(MatSound::timeLeft),
                    Codec.INT.fieldOf("maxTime").forGetter(MatSound::maxTime),
                    Codec.INT.fieldOf("startTime").forGetter(MatSound::startTime),
                    Codec.INT.fieldOf("length").forGetter(MatSound::length),
                    Codec.FLOAT.fieldOf("frequency").forGetter(MatSound::frequency),
                    Codec.FLOAT.fieldOf("intensity").forGetter(MatSound::intensity))
            .apply(instance, MatSound::new));

    @Override
    public Identifier id() {
        return this.id;
    }
}
