package mdteam.ait.datagen.datagen_providers.loot;

import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.tag.ItemTags;

public class AITBlockLootTables extends FabricBlockLootTableProvider {
	public AITBlockLootTables(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generate() {
		AITBlocks.getBlocks().stream().filter(block ->
				block != AITBlocks.EXTERIOR_BLOCK && block != AITBlocks.CONSOLE && block != AITBlocks.ZEITON_CLUSTER
		).forEach(this::addDrop);
		this.addDrop(AITBlocks.ZEITON_CLUSTER, (block) ->
				dropsWithSilkTouch(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
						.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0F)))
						.apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
						.conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create()
								.tag(ItemTags.CLUSTER_MAX_HARVESTABLES))).alternatively(
										this.applyExplosionDecay(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
												.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))))));
	}
}
