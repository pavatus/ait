package dev.amble.ait.init;

import dev.amble.lib.api.AmbleKitInitializer;
import dev.amble.lib.register.AmbleRegistries;

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
        AmbleRegistries.getInstance().registerAll(
                SonicRegistry.getInstance(),
                DesktopRegistry.getInstance(),
                ConsoleVariantRegistry.getInstance(),
                MachineRecipeRegistry.getInstance(),
                TravelSoundRegistry.getInstance(),
                FlightSoundRegistry.getInstance(),
                VortexReferenceRegistry.getInstance(),
                BlueprintRegistry.getInstance(),
                ExteriorVariantRegistry.getInstance(),
                CategoryRegistry.getInstance(),
                TardisComponentRegistry.getInstance(),
                LockedDimensionRegistry.getInstance(),
                HumRegistry.getInstance(),
                SubSystemRegistry.getInstance(),
                ItemOpinionRegistry.getInstance()
        );
    }
}
