package loqor.ait.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class AITRegistryEvents {
    public static final Event<Defaults> EXTERIOR_DEFAULTS = EventFactory.createArrayBacked(Defaults.class, callbacks -> () -> {
        for (Defaults callback : callbacks) {
            callback.defaults();
        }
    });

    /**
     * Called when a Registries is initialised
     */
    @FunctionalInterface
    public interface Defaults {
        void defaults();
    }
}
