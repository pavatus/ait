package dev.amble.ait.init;

import dev.amble.lib.api.AmbleKitInitializer;
import dev.amble.lib.register.api.RegistryEvents;

import dev.amble.ait.core.engine.registry.SubSystemRegistry;
import dev.amble.ait.core.item.blueprint.BlueprintRegistry;
import dev.amble.ait.core.likes.ItemOpinionRegistry;
import dev.amble.ait.core.lock.LockedDimensionRegistry;
import dev.amble.ait.core.sounds.flight.FlightSoundRegistry;
import dev.amble.ait.core.sounds.travel.TravelSoundRegistry;
import dev.amble.ait.core.tardis.vortex.reference.VortexReferenceRegistry;
import dev.amble.ait.registry.impl.*;
import dev.amble.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import dev.amble.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class AmbleInitializer implements AmbleKitInitializer {
    @Override
    public void onInitialize() {
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
