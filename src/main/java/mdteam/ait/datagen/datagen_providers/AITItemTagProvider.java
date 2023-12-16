package mdteam.ait.datagen.datagen_providers;

import mdteam.ait.AITMod;
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

    public static final TagKey<Item> TEST_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(AITMod.MOD_ID, ("test_items")));

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(TEST_ITEMS);
        //.add();
    }
}
