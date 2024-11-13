/*
package loqor.ait.datagen.datagen_providers.loot;


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
*/