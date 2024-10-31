package loqor.ait.datagen.datagen_providers;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITTags;

public class AITBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public AITBlockTagProvider(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(AITBlocks.DOOR_BLOCK).add(AITBlocks.ZEITON_BLOCK)
                .add(AITBlocks.ZEITON_CLUSTER).add(AITBlocks.BUDDING_ZEITON).add(AITBlocks.LARGE_ZEITON_BUD)
                .add(AITBlocks.MEDIUM_ZEITON_BUD).add(AITBlocks.SMALL_ZEITON_BUD).add(AITBlocks.MONITOR_BLOCK)
                .add(AITBlocks.ARTRON_COLLECTOR_BLOCK).add(AITBlocks.CONSOLE_GENERATOR);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(AITBlocks.ZEITON_BLOCK).add(AITBlocks.BUDDING_ZEITON)
                .add(AITBlocks.ZEITON_CLUSTER);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).add(AITBlocks.LARGE_ZEITON_BUD)
                .add(AITBlocks.MEDIUM_ZEITON_BUD).add(AITBlocks.SMALL_ZEITON_BUD).add(AITBlocks.MONITOR_BLOCK);

        getOrCreateTagBuilder(AITTags.Blocks.SONIC_INTERACTABLE).add(Blocks.IRON_DOOR).add(Blocks.IRON_TRAPDOOR)
                .add(Blocks.TNT).add(Blocks.CAMPFIRE).add(Blocks.CANDLE).add(Blocks.CANDLE_CAKE)
                .add(Blocks.WHITE_CANDLE).add(Blocks.ORANGE_CANDLE).add(Blocks.MAGENTA_CANDLE)
                .add(Blocks.LIGHT_BLUE_CANDLE).add(Blocks.YELLOW_CANDLE).add(Blocks.LIME_CANDLE).add(Blocks.PINK_CANDLE)
                .add(Blocks.GRAY_CANDLE).add(Blocks.LIGHT_GRAY_CANDLE).add(Blocks.CYAN_CANDLE).add(Blocks.PURPLE_CANDLE)
                .add(Blocks.BLUE_CANDLE).add(Blocks.BROWN_CANDLE).add(Blocks.GREEN_CANDLE).add(Blocks.RED_CANDLE)
                .add(Blocks.BLACK_CANDLE).add(Blocks.CANDLE_CAKE).add(Blocks.WHITE_CANDLE_CAKE)
                .add(Blocks.ORANGE_CANDLE_CAKE).add(Blocks.MAGENTA_CANDLE_CAKE).add(Blocks.LIGHT_BLUE_CANDLE_CAKE)
                .add(Blocks.YELLOW_CANDLE_CAKE).add(Blocks.LIME_CANDLE_CAKE).add(Blocks.PINK_CANDLE_CAKE)
                .add(Blocks.GRAY_CANDLE_CAKE).add(Blocks.LIGHT_GRAY_CANDLE_CAKE).add(Blocks.CYAN_CANDLE_CAKE)
                .add(Blocks.PURPLE_CANDLE_CAKE).add(Blocks.BLUE_CANDLE_CAKE).add(Blocks.BROWN_CANDLE_CAKE)
                .add(Blocks.GREEN_CANDLE_CAKE).add(Blocks.RED_CANDLE_CAKE).add(Blocks.BLACK_CANDLE_CAKE)
                .add(Blocks.REDSTONE_LAMP).add(AITBlocks.EXTERIOR_BLOCK).add(AITBlocks.CONSOLE_GENERATOR)
                .add(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE).add(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE)
                .add(AITBlocks.MACHINE_CASING);

        getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE).add(AITBlocks.EXTERIOR_BLOCK, AITBlocks.CONSOLE);
        getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE).add(AITBlocks.EXTERIOR_BLOCK, AITBlocks.CONSOLE);

        // Martian Blocks
        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(AITBlocks.MARTIAN_BRICK_WALL);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(AITBlocks.MARTIAN_COBBLESTONE_WALL);

    }
}
