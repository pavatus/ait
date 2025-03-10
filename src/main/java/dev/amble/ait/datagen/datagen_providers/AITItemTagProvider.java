package dev.amble.ait.datagen.datagen_providers;


import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import dev.amble.ait.core.AITItems;
import dev.amble.ait.core.AITTags;
import dev.amble.ait.module.ModuleRegistry;

public class AITItemTagProvider extends FabricTagProvider<Item> {
    public AITItemTagProvider(FabricDataOutput output,
            @Nullable CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.ITEM, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Items
        getOrCreateTagBuilder(AITTags.Items.SONIC_ITEM).add(AITItems.SONIC_SCREWDRIVER);

        getOrCreateTagBuilder(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(AITItems.DRIFTING_MUSIC_DISC)
                .add(AITItems.MERCURY_MUSIC_DISC).add(AITItems.WONDERFUL_TIME_IN_SPACE_MUSIC_DISC)
                .add(AITItems.EARTH_MUSIC_DISC).add(AITItems.VENUS_MUSIC_DISC);

        getOrCreateTagBuilder(AITTags.Items.CLUSTER_MAX_HARVESTABLES).add(AITItems.ZEITON_SHARD);

        getOrCreateTagBuilder(AITTags.Items.NO_BOP).add(AITItems.SONIC_SCREWDRIVER);

        getOrCreateTagBuilder(AITTags.Items.FULL_RESPIRATORS).add(AITItems.RESPIRATOR);

        getOrCreateTagBuilder(AITTags.Items.HALF_RESPIRATORS).add(AITItems.FACELESS_RESPIRATOR);

        /*if (isUnlockedOnThisDay(Calendar.DECEMBER, 27)) {
            getOrCreateTagBuilder(AITTags.Items.HALF_RESPIRATORS).add(AITItems.SANTA_HAT);
        }*/

        getOrCreateTagBuilder(AITTags.Items.KEY).add(AITItems.IRON_KEY, AITItems.GOLD_KEY, AITItems.CLASSIC_KEY,
                AITItems.NETHERITE_KEY, AITItems.SKELETON_KEY);

        /*getOrCreateTagBuilder(AITTags.Items.GOAT_HORN).add(AITItems.IRON_GOAT_HORN, AITItems.GOLD_GOAT_HORN, AITItems.CLASSIC_GOAT_HORN,
                AITItems.NETHERITE_GOAT_HORN);*/

        getOrCreateTagBuilder(AITTags.Items.REPAIRS_SUBSYSTEM).add(Items.IRON_INGOT, AITItems.ZEITON_SHARD, Items.COPPER_INGOT, Items.AMETHYST_SHARD);

        getOrCreateTagBuilder(AITTags.Items.IS_TARDIS_FUEL).add(AITItems.ZEITON_DUST, AITItems.ZEITON_SHARD);
        getOrCreateTagBuilder(AITTags.Items.IS_TARDIS_FUEL).forceAddTag(ItemTags.LOGS_THAT_BURN);
        getOrCreateTagBuilder(AITTags.Items.IS_TARDIS_FUEL).forceAddTag(ItemTags.COALS);
        getOrCreateTagBuilder(AITTags.Items.IS_TARDIS_FUEL).add(Items.LAVA_BUCKET);

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
