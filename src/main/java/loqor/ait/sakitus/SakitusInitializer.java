package loqor.ait.sakitus;

import dev.pavatus.lib.api.SakitusModInitializer;
import dev.pavatus.lib.register.api.RegistryEvents;

import loqor.ait.core.engine.registry.SubSystemRegistry;
import loqor.ait.core.item.blueprint.BlueprintRegistry;
import loqor.ait.core.likes.ItemOpinionRegistry;
import loqor.ait.core.lock.LockedDimensionRegistry;
import loqor.ait.core.sounds.flight.FlightSoundRegistry;
import loqor.ait.core.sounds.travel.TravelSoundRegistry;
import loqor.ait.core.tardis.vortex.reference.VortexReferenceRegistry;
import loqor.ait.registry.impl.*;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class SakitusInitializer implements SakitusModInitializer {
    @Override
    public void onInitializeSakitus() {
        RegistryEvents.INIT.register((registries, isClient) -> {
            if (isClient) return;

            registries.register(SonicRegistry.getInstance());
            registries.register(DesktopRegistry.getInstance());
            registries.register(ConsoleVariantRegistry.getInstance());
            registries.register(MachineRecipeRegistry.getInstance());
            registries.register(TravelSoundRegistry.getInstance());
            registries.register(FlightSoundRegistry.getInstance());
            registries.register(VortexReferenceRegistry.getInstance());
            registries.register(BlueprintRegistry.getInstance());
            registries.register(ExteriorVariantRegistry.getInstance());
            registries.register(CategoryRegistry.getInstance());
            registries.register(TardisComponentRegistry.getInstance());
            registries.register(LockedDimensionRegistry.getInstance());
            registries.register(HumRegistry.getInstance());
            registries.register(SubSystemRegistry.getInstance());
            registries.register(ItemOpinionRegistry.getInstance());
        });
    }
}
