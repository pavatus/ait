package loqor.ait.datagen.datagen_providers;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.core.PlanetBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITTags;
import loqor.ait.datagen.datagen_providers.loot.AITBlockLootTables;
import loqor.ait.datagen.datagen_providers.util.PickaxeMineable;


public class AITBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public AITBlockTagProvider(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        FabricTagBuilder builder = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
        for (Map.Entry<Block, Annotation> entry : AITBlockLootTables.getAnnotatedBlocks(PickaxeMineable.class)) {
            builder.add(entry.getKey());

            PickaxeMineable annotation = (PickaxeMineable) entry.getValue();
            if (annotation.tool() != PickaxeMineable.Tool.NONE) {
                getOrCreateTagBuilder(annotation.tool().tag).add(entry.getKey());
            }
        }
//TODO: Make the glass tag work on this and the leafs, for now theres just glass and glass pane and the birch leafs as a temporarly thing.
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
                .add(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE).add(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.LAPIS_ORE,Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE)
                .add(Blocks.BRICKS, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE)
                .add(Blocks.BIRCH_LEAVES)
                .add(PlanetBlocks.ANORTHOSITE_IRON_ORE, PlanetBlocks.MARTIAN_IRON_ORE, PlanetBlocks.ANORTHOSITE_GOLD_ORE, PlanetBlocks.MARTIAN_GOLD_ORE, PlanetBlocks.ANORTHOSITE_COAL_ORE, PlanetBlocks.MARTIAN_COAL_ORE, PlanetBlocks.ANORTHOSITE_LAPIS_ORE, PlanetBlocks.MARTIAN_LAPIS_ORE, PlanetBlocks.ANORTHOSITE_DIAMOND_ORE, PlanetBlocks.MARTIAN_DIAMOND_ORE)
                .add(AITBlocks.MACHINE_CASING, AITBlocks.CONSOLE);


        getOrCreateTagBuilder(BlockTags.DIRT).add(PlanetBlocks.MARTIAN_SAND).add(PlanetBlocks.REGOLITH);

        getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE).add(AITBlocks.EXTERIOR_BLOCK, AITBlocks.CONSOLE);
        getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE).add(AITBlocks.EXTERIOR_BLOCK, AITBlocks.CONSOLE);

        getOrCreateTagBuilder(AITTags.Blocks.FLUID_LINK_CAN_CONNECT).add(Blocks.JUKEBOX);

        ModuleRegistry.instance().iterator().forEachRemaining(module -> {
            module.getDataGenerator().ifPresent(generator -> {
                generator.blockTags(this);
            });
        });
    }

    @Override
    public FabricTagProvider<Block>.FabricTagBuilder getOrCreateTagBuilder(TagKey<Block> tag) {
        return super.getOrCreateTagBuilder(tag);
    }
}
