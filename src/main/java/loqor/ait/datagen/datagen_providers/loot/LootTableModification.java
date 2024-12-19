
package loqor.ait.datagen.datagen_providers.loot;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;

import loqor.ait.core.AITItems;
import loqor.ait.registry.impl.BlueprintRegistry;

public class LootTableModification implements ModInitializer {

    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()
                    && (id.equals(LootTables.NETHER_BRIDGE_CHEST) || id.equals(LootTables.DESERT_PYRAMID_CHEST)
                            || id.equals(LootTables.VILLAGE_ARMORER_CHEST))
                    || id.equals(LootTables.END_CITY_TREASURE_CHEST) || id.equals(LootTables.SHIPWRECK_MAP_CHEST)
                    || id.equals(LootTables.SIMPLE_DUNGEON_CHEST) || id.equals(LootTables.STRONGHOLD_LIBRARY_CHEST)) {

                LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(AITItems.BLUEPRINT).weight(10)
                        .apply(SetBlueprintLootFunction.builder(BlueprintRegistry.getRandomEntry())));

                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
