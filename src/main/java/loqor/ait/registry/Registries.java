package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;
import loqor.ait.registry.impl.*;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// TODO: move all registries over to here
public class Registries {

    private static Registries INSTANCE;
    private final List<Registry> registries = new ArrayList<>();

    private Registries() {
        registries.add(SonicRegistry.getInstance());
        registries.add(DesktopRegistry.getInstance());
        registries.add(ConsoleVariantRegistry.getInstance());
        registries.add(MachineRecipeRegistry.getInstance());
        registries.add(ExteriorVariantRegistry.getInstance());
        registries.add(CategoryRegistry.getInstance());
    }

    public void subscribe(InitType env) {
        for (Registry registry : registries) {
            AITMod.LOGGER.info("Delegating to " + registry.getClass().getName());
            if (env == InitType.COMMON)
                registry.init();

            if (registry instanceof SimpleDatapackRegistry<?> simple)
                env.init(simple);
        }
    }

    public static Registries getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Registries();

        return INSTANCE;
    }

    public enum InitType {
        CLIENT(SimpleDatapackRegistry::onClientInit),
        SERVER(SimpleDatapackRegistry::onServerInit),
        COMMON(SimpleDatapackRegistry::onCommonInit);

        private final Consumer<SimpleDatapackRegistry<?>> consumer;

        InitType(Consumer<SimpleDatapackRegistry<?>> consumer) {
            this.consumer = consumer;
        }

        public void init(SimpleDatapackRegistry<?> registry) {
            this.consumer.accept(registry);
        }
    }
}
