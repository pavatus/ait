package loqor.ait.registry.unlockable;

import loqor.ait.AITMod;
import loqor.ait.registry.DatapackRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.wrapper.server.ServerTardis;

import java.util.function.Consumer;

public abstract class UnlockableRegistry<T extends Unlockable> extends DatapackRegistry<T> {

    public void unlock(Tardis tardis, Loyalty loyalty, Consumer<T> consumer) {
        if (!(tardis instanceof ServerTardis serverTardis))
            return;

        for (T schema : REGISTRY.values()) {
            if (!schema.getRequirement().greaterOrEqual(loyalty) || serverTardis.isUnlocked(schema))
                continue;

            AITMod.LOGGER.debug("Unlocked " + schema.unlockType() + " " + schema.id() + " for tardis [" + tardis.getUuid() + "]");
            serverTardis.unlock(schema);

            if (consumer != null)
                consumer.accept(schema);
        }
    }
}
