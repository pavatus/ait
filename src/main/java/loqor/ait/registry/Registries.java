package loqor.ait.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import loqor.ait.core.lock.LockedDimension;
import loqor.ait.core.lock.LockedDimensionRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import loqor.ait.core.planet.PlanetRegistry;
import loqor.ait.registry.impl.*;
import loqor.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

// TODO: move all registries over to here
public class Registries {

    private static Registries INSTANCE;
    private final List<Registry> registries = new ArrayList<>();

    private Registries() {
        this.onCommonInit();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            this.onClientInit();
    }

    private void onCommonInit() {
        registries.add(SonicRegistry.getInstance());
        registries.add(DesktopRegistry.getInstance());
        registries.add(ConsoleVariantRegistry.getInstance());
        registries.add(MachineRecipeRegistry.getInstance());
        registries.add(ExteriorVariantRegistry.getInstance());
        registries.add(CategoryRegistry.getInstance());
        registries.add(TardisComponentRegistry.getInstance());
        registries.add(PlanetRegistry.getInstance());
        registries.add(LockedDimensionRegistry.getInstance());
    }

    private void onClientInit() {
        registries.add(ClientConsoleVariantRegistry.getInstance());
        registries.add(ClientExteriorVariantRegistry.getInstance());
    }

    public void subscribe(InitType env) {
        if (env == InitType.CLIENT && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new UnsupportedOperationException("Cannot call onInitializeClient while not running a client!");

        for (Registry registry : registries) {
            env.init(registry);
        }
    }

    public static Registries getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Registries();

        return INSTANCE;
    }

    public enum InitType {
        CLIENT(Registry::onClientInit), SERVER(Registry::onServerInit), COMMON(Registry::onCommonInit);

        private final Consumer<Registry> consumer;

        InitType(Consumer<Registry> consumer) {
            this.consumer = consumer;
        }

        public void init(Registry registry) {
            this.consumer.accept(registry);
        }
    }
}
