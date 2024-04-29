package loqor.ait.registry.unlockable;

import com.mojang.serialization.Codec;
import loqor.ait.AITMod;
import loqor.ait.registry.datapack.DatapackRegistry;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class UnlockableRegistry<T extends Unlockable> extends SimpleDatapackRegistry<T> {

    protected UnlockableRegistry(Function<InputStream, T> deserializer, Codec<T> codec, Identifier packet, Identifier name, boolean sync) {
        super(deserializer, codec, packet, name, sync);
    }

    protected UnlockableRegistry(Function<InputStream, T> deserializer, Codec<T> codec, String packet, String name, boolean sync) {
        super(deserializer, codec, packet, name, sync);
    }

    protected UnlockableRegistry(Function<InputStream, T> deserializer, Codec<T> codec, String name, boolean sync) {
        super(deserializer, codec, name, sync);
    }

    @Override
    @Deprecated
    public T getRandom(Random random) {
        AITMod.LOGGER.warn("Using plain random in an unlockable registry! Class: " + this.getClass());
        return super.getRandom(random);
    }

    @Override
    @Deprecated
    public T getRandom() {
        return super.getRandom();
    }

    public T getRandom(Tardis tardis, Random random) {
        return DatapackRegistry.getRandom(this.toList().stream().filter(tardis::isUnlocked).toList(), random, this.fallback());
    }

    public T getRandom(Tardis tardis) {
        return this.getRandom(tardis, RANDOM);
    }

    public void unlock(Tardis tardis, Loyalty loyalty, Consumer<T> consumer) {
        if (!(tardis instanceof ServerTardis serverTardis))
            return;

        for (T schema : REGISTRY.values()) {
            if (schema.getRequirement() == Loyalty.MIN)
                continue;

            if (serverTardis.isUnlocked(schema))
                continue;

            if (!schema.getRequirement().greaterOrEqual(loyalty))
                continue;

            AITMod.LOGGER.debug("Unlocked " + schema.unlockType() + " "
                    + schema.id() + " for tardis [" + tardis.getUuid() + "]");

            serverTardis.unlock(schema);

            if (consumer != null)
                consumer.accept(schema);
        }
    }
}
