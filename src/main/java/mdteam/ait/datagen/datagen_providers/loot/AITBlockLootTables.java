package mdteam.ait.datagen.datagen_providers.loot;

import mdteam.ait.core.AITBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class AITBlockLootTables extends FabricBlockLootTableProvider {
	public AITBlockLootTables(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generate() {
		AITBlocks.getBlocks().stream().filter(block ->
				block != AITBlocks.EXTERIOR_BLOCK && block != AITBlocks.CONSOLE
		).forEach(this::addDrop);
		/*this.addDrop(AITBlocks.DOOR_BLOCK);
		this.addDrop(AITBlocks.ZEITON_BLOCK);
		this.addDrop(AITBlocks.ZEITON_CLUSTER);
		this.addDrop(AITBlocks.BUDDING_ZEITON);
		this.addDrop(AITBlocks.LARGE_ZEITON_BUD);
		this.addDrop(AITBlocks.MEDIUM_ZEITON_BUD);
		this.addDrop(AITBlocks.SMALL_ZEITON_BUD);
		this.addDrop(AITBlocks.MONITOR_BLOCK);
		this.addDrop(AITBlocks.DETECTOR_BLOCK);
		this.addDrop(AITBlocks.CORAL_PLANT);
		this.addDrop(AITBlocks.CONSOLE_GENERATOR);
		this.addDrop(AITBlocks.PLAQUE_BLOCK);
		this.addDrop(AITBlocks.ARTRON_COLLECTOR_BLOCK);*/
	}
}
