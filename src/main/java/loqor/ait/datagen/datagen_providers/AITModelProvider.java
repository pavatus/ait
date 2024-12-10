package loqor.ait.datagen.datagen_providers;

import static loqor.ait.datagen.datagen_providers.loot.AITBlockLootTables.filterBlocksWithAnnotation;

import java.util.ArrayList;
import java.util.List;

import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.core.PlanetItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import loqor.ait.core.AITBlocks;
import loqor.ait.datagen.datagen_providers.util.AutomaticModel;


public class AITModelProvider extends FabricModelProvider {
    private final List<Block> directionalBlocksToRegister = new ArrayList<>();
    private final List<Block> simpleBlocksToRegister = new ArrayList<>();

    public AITModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        filterBlocksWithAnnotation(AITBlocks.get(), AutomaticModel.class, false).forEach(generator::registerSimpleCubeAll);

        for (Block block : directionalBlocksToRegister) {
            // Identifier identifier = new
            // Identifier(block.getTranslationKey().split("\\.")[1]);
            generator.blockStateCollector.accept(MultipartBlockStateSupplier.create(block).with(
                    When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                    BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R0)));
        }
        for (Block block : simpleBlocksToRegister) {
            generator.registerSimpleCubeAll(block);
        }

        ModuleRegistry.instance().iterator().forEachRemaining(module -> module.getDataGenerator().ifPresent(data -> data.models(generator)));
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // filterBlocksWithAnnotation(AITBlocks.get(), AutomaticModel.class, false).forEach(b -> generator.register(b.asItem(), Models.));
        generator.register(PlanetItems.MARTIAN_STONE_SWORD, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_SHOVEL, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_PICKAXE, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_HOE, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_AXE, Models.HANDHELD);

        generator.register(PlanetItems.ANORTHOSITE_SWORD, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_SHOVEL, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_PICKAXE, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_HOE, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_AXE, Models.HANDHELD);


    }

    public void registerDirectionalBlock(Block block) {
        directionalBlocksToRegister.add(block);
    }

    public void registerSimpleBlock(Block block) {
        simpleBlocksToRegister.add(block);
    }
}
