package mdteam.ait.datagen.datagen_providers.loot;

import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import mdteam.ait.core.AITBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;

import java.util.Set;

public class AITBlockLootTables extends FabricBlockLootTableProvider {
    public AITBlockLootTables(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        for (Block block : AITBlocks.getBlocks()) {
            this.addDrop(block);
        }
    }
}
