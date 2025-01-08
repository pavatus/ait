package loqor.ait.datagen.datagen_providers;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;

import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITTags;


public class AITEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public AITEntityTypeTagProvider(FabricDataOutput output,
                                    CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(AITTags.EntityTypes.NON_DISMOUNTABLE)
                .add(AITEntityTypes.FLIGHT_TARDIS_TYPE);

        getOrCreateTagBuilder(AITTags.EntityTypes.BOSS)
                .add(EntityType.ENDER_DRAGON).add(EntityType.WITHER)
                .add(EntityType.WARDEN).add(EntityType.ELDER_GUARDIAN);
    }
}
