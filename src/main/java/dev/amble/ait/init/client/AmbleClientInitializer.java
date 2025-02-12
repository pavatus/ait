package dev.amble.ait.init.client;

import dev.amble.lib.api.AmbleKitClientInitializer;
import dev.amble.lib.register.api.RegistryEvents;

import dev.amble.ait.client.AITModClient;
import dev.amble.ait.registry.impl.SonicRegistry;
import dev.amble.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import dev.amble.ait.registry.impl.exterior.ClientExteriorVariantRegistry;

public class AmbleClientInitializer implements AmbleKitClientInitializer {
    @Override
    public void onInitialize() {
        RegistryEvents.INIT.register((registries, isClient) -> {
            if (!isClient) return;

            registries.register(SonicRegistry.getInstance(), true);
            registries.register(ClientExteriorVariantRegistry.getInstance(), true);
            registries.register(ClientConsoleVariantRegistry.getInstance(), true);
        });

        AITModClient.sonicModelPredicate();
    }
}
