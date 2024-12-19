package dev.pavatus.register.api;

import dev.pavatus.register.Registries;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class RegistryEvents {
    public static final Event<Subscribe> SUBSCRIBE = EventFactory.createArrayBacked(Subscribe.class, callbacks -> (registries, env) -> {
        for (Subscribe callback : callbacks) {
            callback.subscribe(registries, env);
        }
    });

    public static final Event<Init> INIT = EventFactory.createArrayBacked(Init.class, callbacks -> (registries, isClient) -> {
        for (Init callback : callbacks) {
            callback.init(registries, isClient);
        }
    });

    /**
     * Called when a Registries is subscribed
     */
    @FunctionalInterface
    public interface Subscribe {
        void subscribe(Registries registries, Registries.InitType env);
    }

    /**
     * Called when Registries is initialized
     */
    @FunctionalInterface
    public interface Init {
        void init(Registries registries, boolean isClient);
    }
}
