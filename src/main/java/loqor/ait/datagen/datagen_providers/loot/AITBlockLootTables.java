package loqor.ait.datagen.datagen_providers.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITTags;

public class AITBlockLootTables extends FabricBlockLootTableProvider {
    public AITBlockLootTables(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        AITBlocks.getBlocks().stream().filter(block -> block != AITBlocks.EXTERIOR_BLOCK && block != AITBlocks.CONSOLE
                && block != AITBlocks.ZEITON_CLUSTER).forEach(this::addDrop);
        this.addDrop(AITBlocks.ZEITON_CLUSTER,
                (block) -> dropsWithSilkTouch(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                        .conditionally(MatchToolLootCondition
                                .builder(ItemPredicate.Builder.create().tag(AITTags.Items.CLUSTER_MAX_HARVESTABLES)))
                        .alternatively(this.applyExplosionDecay(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))))));


        addDrop(AITBlocks.WAYPOINT_BANK, doorDrops(AITBlocks.WAYPOINT_BANK));

        // Martian
        addDrop(AITBlocks.MARTIAN_STONE);
        addDrop(AITBlocks.MARTIAN_STONE_SLAB);
        addDrop(AITBlocks.MARTIAN_STONE_WALL);
        addDrop(AITBlocks.MARTIAN_STONE_STAIRS);
        addDrop(AITBlocks.MARTIAN_STONE_BUTTON);
        addDrop(AITBlocks.MARTIAN_STONE_PRESSURE_PLATE);
        addDrop(AITBlocks.POLISHED_MARTIAN_STONE);
        addDrop(AITBlocks.POLISHED_MARTIAN_STONE_STAIRS);
        addDrop(AITBlocks.POLISHED_MARTIAN_STONE_SLAB);
        addDrop(AITBlocks.MARTIAN_PILLAR);
        addDrop(AITBlocks.MARTIAN_STONE);
        addDrop(AITBlocks.MARTIAN_BRICKS);
        addDrop(AITBlocks.MARTIAN_BRICK_SLAB, slabDrops(AITBlocks.MARTIAN_BRICK_SLAB));
        addDrop(AITBlocks.MARTIAN_BRICK_STAIRS);
        addDrop(AITBlocks.MARTIAN_BRICK_WALL);
        addDrop(AITBlocks.CRACKED_MARTIAN_BRICKS);
        addDrop(AITBlocks.MARTIAN_COBBLESTONE);
        addDrop(AITBlocks.MARTIAN_COBBLESTONE_SLAB, slabDrops(AITBlocks.MARTIAN_COBBLESTONE_SLAB));
        addDrop(AITBlocks.MARTIAN_COBBLESTONE_WALL);
        addDrop(AITBlocks.MARTIAN_COBBLESTONE_STAIRS);
        addDrop(AITBlocks.SMOOTH_MARTIAN_STONE);
        addDrop(AITBlocks.SMOOTH_MARTIAN_STONE_SLAB, slabDrops(AITBlocks.SMOOTH_MARTIAN_STONE_SLAB));
    }
}
