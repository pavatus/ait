package mdteam.ait.datagen.datagen_providers;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.util.AITModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AITItemTagProvider extends FabricTagProvider<Item> {
    public AITItemTagProvider(FabricDataOutput output, @Nullable CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.ITEM, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(AITModTags.Items.SONIC_ITEM)
                .add(AITItems.MECHANICAL_SONIC_SCREWDRIVER)
                .add(AITItems.CORAL_SONIC_SCREWDRIVER)
                .add(AITItems.RENAISSANCE_SONIC_SCREWDRIVER);
    }
}
