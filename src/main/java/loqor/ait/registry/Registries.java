package loqor.ait.registry;

import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.MachineRecipeRegistry;
import loqor.ait.registry.impl.SonicRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

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
        if (env == InitType.CLIENT && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new UnsupportedOperationException("Cannot call onInitializeClient while not running a client!");

        for (Registry registry : registries) {
            if (env == InitType.COMMON)
                registry.init();

            env.init(registry);
        }
    }

    public static Registries getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Registries();

        return INSTANCE;
    }

    public enum InitType {
        CLIENT(Registry::onClientInit),
        SERVER(Registry::onServerInit),
        COMMON(Registry::onCommonInit); // handled in #init

        private final Consumer<Registry> consumer;

        InitType(Consumer<Registry> consumer) {
            this.consumer = consumer;
        }

        public void init(Registry registry) {
            this.consumer.accept(registry);
        }
    }
}
