package mdteam.ait.datagen.datagen_providers;

import mdteam.ait.core.AITBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class AITBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public AITBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(AITBlocks.DOOR_BLOCK)
				.add(AITBlocks.ZEITON_BLOCK)
				.add(AITBlocks.ZEITON_CLUSTER)
				.add(AITBlocks.BUDDING_ZEITON)
				.add(AITBlocks.LARGE_ZEITON_BUD)
				.add(AITBlocks.MEDIUM_ZEITON_BUD)
				.add(AITBlocks.SMALL_ZEITON_BUD)
				.add(AITBlocks.MONITOR_BLOCK)
				.add(AITBlocks.ARTRON_COLLECTOR_BLOCK)
				.add(AITBlocks.CONSOLE_GENERATOR);

		getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
				.add(AITBlocks.ZEITON_BLOCK)
				.add(AITBlocks.BUDDING_ZEITON)
				.add(AITBlocks.ZEITON_CLUSTER);

		getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
				.add(AITBlocks.LARGE_ZEITON_BUD)
				.add(AITBlocks.MEDIUM_ZEITON_BUD)
				.add(AITBlocks.SMALL_ZEITON_BUD)
				.add(AITBlocks.MONITOR_BLOCK);
	}
}
