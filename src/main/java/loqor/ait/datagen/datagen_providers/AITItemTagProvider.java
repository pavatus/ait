package loqor.ait.datagen.datagen_providers;

import java.util.concurrent.CompletableFuture;

import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.core.PlanetItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import loqor.ait.core.AITItems;
import loqor.ait.core.AITTags;

public class AITItemTagProvider extends FabricTagProvider<Item> {
    public AITItemTagProvider(FabricDataOutput output,
            @Nullable CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.ITEM, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Items
        getOrCreateTagBuilder(AITTags.Items.SONIC_ITEM).add(AITItems.SONIC_SCREWDRIVER);

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR).add(PlanetItems.SPACESUIT_BOOTS).add(PlanetItems.SPACESUIT_LEGGINGS).add(PlanetItems.SPACESUIT_CHESTPLATE).add(PlanetItems.SPACESUIT_HELMET);

        getOrCreateTagBuilder(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(AITItems.DRIFTING_MUSIC_DISC)
                .add(AITItems.MERCURY_MUSIC_DISC);

        getOrCreateTagBuilder(AITTags.Items.CLUSTER_MAX_HARVESTABLES).add(AITItems.ZEITON_SHARD);

        getOrCreateTagBuilder(AITTags.Items.NO_BOP).add(AITItems.SONIC_SCREWDRIVER);

        getOrCreateTagBuilder(AITTags.Items.FULL_RESPIRATORS).add(AITItems.RESPIRATOR);

        getOrCreateTagBuilder(AITTags.Items.HALF_RESPIRATORS).add(AITItems.FACELESS_RESPIRATOR);

        getOrCreateTagBuilder(AITTags.Items.KEY).add(AITItems.IRON_KEY, AITItems.GOLD_KEY, AITItems.CLASSIC_KEY,
                AITItems.NETHERITE_KEY, AITItems.SKELETON_KEY);

        ModuleRegistry.instance().iterator().forEachRemaining(module -> {
            module.getDataGenerator().ifPresent(generator -> {
                generator.itemTags(this);
            });
        });
    }

    @Override
    public FabricTagProvider<Item>.FabricTagBuilder getOrCreateTagBuilder(TagKey<Item> tag) {
        return super.getOrCreateTagBuilder(tag);
    }
}
