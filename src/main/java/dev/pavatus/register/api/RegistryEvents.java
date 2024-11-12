package dev.pavatus.register.api;

import dev.pavatus.register.Registries;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class RegistryEvents {
    public static final Event<Init> INIT = EventFactory.createArrayBacked(Init.class, callbacks -> (registries, env) -> {
        for (Init callback : callbacks) {
            callback.init(registries, env);
        }
    });

    /**
     * Called when a Registries is initialised
     */
    @FunctionalInterface
    public interface Init {
        void init(Registries registries, Registries.InitType env);
    }
}
