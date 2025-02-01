package loqor.ait.sakitus.client;

import dev.pavatus.lib.api.SakitusClientModInitializer;
import dev.pavatus.lib.register.api.RegistryEvents;

import loqor.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;

public class SakitusClientInitializer implements SakitusClientModInitializer {
    @Override
    public void onInitializeSakitus() {
        RegistryEvents.INIT.register((registries, isClient) -> {
            if (!isClient) return;

            registries.register(ClientExteriorVariantRegistry.getInstance(), true);
            registries.register(ClientConsoleVariantRegistry.getInstance(), true);
        });
    }
}
