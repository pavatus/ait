package dev.pavatus.register.api;

import dev.pavatus.register.Registries;
import dev.pavatus.register.datapack.SimpleDatapackRegistry;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.util.Identifier;

public final class RegistryEvents {
    public static final Event<Init> INIT = EventFactory.createArrayBacked(Init.class, callbacks -> (registries, env) -> {
        for (Init callback : callbacks) {
            callback.init(registries, env);
        }
    });

    public static final Event<Default> REGISTER_DEFAULTS = EventFactory.createArrayBacked(Default.class, callbacks -> (name, registry) -> {
        for (Default callback : callbacks) {
            callback.register(name, registry);
        }
    });

    /**
     * Called when a Registries is initialised
     */
    @FunctionalInterface
    public interface Init {
        void init(Registries registries, Registries.InitType env);
    }

    /**
     * Called when a registry requests its defaults to be registered
     */
    @FunctionalInterface
    public interface Default {
        void register(Identifier registryName, SimpleDatapackRegistry<?> registry);
    }
}
