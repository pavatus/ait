package dev.amble.ait.init.client;

import dev.amble.lib.api.AmbleKitClientInitializer;
import dev.amble.lib.register.AmbleRegistries;

import dev.amble.ait.registry.impl.SonicRegistry;
import dev.amble.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import dev.amble.ait.registry.impl.exterior.ClientExteriorVariantRegistry;

public class AmbleClientInitializer implements AmbleKitClientInitializer {

    @Override
    public void onInitialize() {
        AmbleRegistries.getInstance().registerAll(
                SonicRegistry.getInstance(),
                ClientExteriorVariantRegistry.getInstance(),
                ClientConsoleVariantRegistry.getInstance()
        );
    }
}
