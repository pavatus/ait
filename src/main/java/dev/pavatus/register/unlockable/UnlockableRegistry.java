package dev.pavatus.register.unlockable;

import java.io.InputStream;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import dev.pavatus.register.datapack.DatapackRegistry;
import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.data.Loyalty;

public abstract class UnlockableRegistry<T extends Unlockable> extends SimpleDatapackRegistry<T> {

    protected UnlockableRegistry(Function<InputStream, T> deserializer, Codec<T> codec, Identifier packet,
            Identifier name, boolean sync) {
        super(deserializer, codec, packet, name, sync);
    }

    protected UnlockableRegistry(Function<InputStream, T> deserializer, Codec<T> codec, String packet, String name,
            boolean sync) {
        super(deserializer, codec, packet, name, sync);
    }

    protected UnlockableRegistry(Function<InputStream, T> deserializer, Codec<T> codec, String name, boolean sync) {
        super(deserializer, codec, name, sync);
    }

    @Override
    @Deprecated
    public T getRandom(Random random) {
        AITMod.LOGGER.warn("Using plain random in an unlockable registry! Class: {}", this.getClass());
        return super.getRandom(random);
    }

    @Override
    @Deprecated
    public T getRandom() {
        return super.getRandom();
    }

    public T getRandom(Tardis tardis, Random random) {
        return DatapackRegistry.getRandom(this.toList().stream().filter(tardis::isUnlocked).toList(), random,
                this::fallback);
    }

    public T getRandom(Tardis tardis) {
        return this.getRandom(tardis, RANDOM);
    }

    public boolean tryUnlock(Tardis tardis, Loyalty loyalty, Consumer<T> consumer) {
        boolean success = false;
        for (T schema : REGISTRY.values()) {
            if (tardis.isUnlocked(schema))
                continue;

            Optional<Loyalty> optional = schema.requirement();

            if (optional.isEmpty() || loyalty.smallerThan(optional.get()))
                continue;

            AITMod.LOGGER.debug("Unlocked {} {} for tardis [{}]", schema.unlockType(), schema.id(), tardis.getUuid());

            success = true;
            tardis.stats().unlock(schema);

            if (consumer != null)
                consumer.accept(schema);
        }

        return success;
    }

    public void unlockAll(Tardis tardis) {
        for (T schema : REGISTRY.values()) {
            tardis.stats().unlock(schema);
        }

        AITMod.LOGGER.debug("Unlocked everything from {} registry for tardis [{}]", this.getClass(), tardis.getUuid());
    }
}