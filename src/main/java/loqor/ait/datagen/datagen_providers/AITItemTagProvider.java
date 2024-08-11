package loqor.ait.datagen.datagen_providers;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import loqor.ait.core.AITItems;
import loqor.ait.core.util.AITModTags;

public class AITItemTagProvider extends FabricTagProvider<Item> {
    public AITItemTagProvider(FabricDataOutput output,
            @Nullable CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.ITEM, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Items
        getOrCreateTagBuilder(AITModTags.Items.SONIC_ITEM).add(AITItems.SONIC_SCREWDRIVER);

        getOrCreateTagBuilder(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(AITItems.DRIFTING_MUSIC_DISC)
                .add(AITItems.MERCURY_MUSIC_DISC);

        getOrCreateTagBuilder(AITModTags.Items.CLUSTER_MAX_HARVESTABLES).add(AITItems.ZEITON_SHARD);

        getOrCreateTagBuilder(AITModTags.Items.NO_BOP).add(AITItems.SONIC_SCREWDRIVER);

        getOrCreateTagBuilder(AITModTags.Items.FULL_RESPIRATORS).add(AITItems.RESPIRATOR);

        getOrCreateTagBuilder(AITModTags.Items.HALF_RESPIRATORS).add(AITItems.FACELESS_RESPIRATOR);

        getOrCreateTagBuilder(AITModTags.Items.KEY).add(AITItems.IRON_KEY, AITItems.GOLD_KEY, AITItems.CLASSIC_KEY,
                AITItems.NETHERITE_KEY, AITItems.SKELETON_KEY);
    }
}
