package loqor.ait.datagen.datagen_providers;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import loqor.ait.core.AITBlocks;

public class AITModelProvider extends FabricModelProvider {
    private final List<Block> directionalBlocksToRegister = new ArrayList<>();
    private final List<Block> simpleBlocksToRegister = new ArrayList<>();

    public AITModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (Block block : directionalBlocksToRegister) {
            // Identifier identifier = new
            // Identifier(block.getTranslationKey().split("\\.")[1]);
            blockStateModelGenerator.blockStateCollector.accept(MultipartBlockStateSupplier.create(block).with(
                    When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                    BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R0)));
        }
        for (Block block : simpleBlocksToRegister) {
            blockStateModelGenerator.registerSimpleCubeAll(block);
        }

        //Martian Stone (Slabs, Walls, etc.)

        BlockStateModelGenerator.BlockTexturePool martian_stone_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.MARTIAN_STONE);
        martian_stone_pool.stairs(AITBlocks.MARTIAN_STONE_STAIRS);
        martian_stone_pool.wall(AITBlocks.MARTIAN_STONE_WALL);
        martian_stone_pool.slab(AITBlocks.MARTIAN_STONE_SLAB);
        martian_stone_pool.button(AITBlocks.MARTIAN_STONE_BUTTON);
        martian_stone_pool.pressurePlate(AITBlocks.MARTIAN_STONE_PRESSURE_PLATE);

        BlockStateModelGenerator.BlockTexturePool martian_bricks_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.MARTIAN_BRICKS);
        martian_bricks_pool.stairs(AITBlocks.MARTIAN_BRICK_STAIRS);
        martian_bricks_pool.wall(AITBlocks.MARTIAN_BRICK_WALL);
        martian_bricks_pool.slab(AITBlocks.MARTIAN_BRICK_SLAB);

        BlockStateModelGenerator.BlockTexturePool martian_cobblestone_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.MARTIAN_COBBLESTONE);
        martian_cobblestone_pool.stairs(AITBlocks.MARTIAN_COBBLESTONE_STAIRS);
        martian_cobblestone_pool.wall(AITBlocks.MARTIAN_COBBLESTONE_WALL);
        martian_cobblestone_pool.slab(AITBlocks.MARTIAN_COBBLESTONE_SLAB);

        BlockStateModelGenerator.BlockTexturePool smooth_martian_stone_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.SMOOTH_MARTIAN_STONE);
        smooth_martian_stone_pool.slab(AITBlocks.SMOOTH_MARTIAN_STONE_SLAB);

        BlockStateModelGenerator.BlockTexturePool polished_martian_stone_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.POLISHED_MARTIAN_STONE);
        polished_martian_stone_pool.stairs(AITBlocks.POLISHED_MARTIAN_STONE_STAIRS);
        polished_martian_stone_pool.slab(AITBlocks.POLISHED_MARTIAN_STONE_SLAB);

        //Martian Stone (Slabs, Walls, etc.)

        BlockStateModelGenerator.BlockTexturePool anorthosite_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.ANORTHOSITE);
        anorthosite_pool.stairs(AITBlocks.ANORTHOSITE_STAIRS);
        anorthosite_pool.wall(AITBlocks.ANORTHOSITE_WALL);
        anorthosite_pool.slab(AITBlocks.ANORTHOSITE_SLAB);
        anorthosite_pool.button(AITBlocks.ANORTHOSITE_BUTTON);
        anorthosite_pool.pressurePlate(AITBlocks.ANORTHOSITE_PRESSURE_PLATE);

        BlockStateModelGenerator.BlockTexturePool anorthosite_bricks_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.ANORTHOSITE_BRICKS);
        anorthosite_bricks_pool.stairs(AITBlocks.ANORTHOSITE_BRICK_STAIRS);
        anorthosite_bricks_pool.wall(AITBlocks.ANORTHOSITE_BRICK_WALL);
        anorthosite_bricks_pool.slab(AITBlocks.ANORTHOSITE_BRICK_SLAB);

        BlockStateModelGenerator.BlockTexturePool smooth_anorthosite_stone_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.SMOOTH_ANORTHOSITE);
        smooth_anorthosite_stone_pool.slab(AITBlocks.SMOOTH_ANORTHOSITE_SLAB);

        BlockStateModelGenerator.BlockTexturePool polished_anorthosite_stone_pool = blockStateModelGenerator.registerCubeAllModelTexturePool(AITBlocks.POLISHED_ANORTHOSITE);
        polished_anorthosite_stone_pool.stairs(AITBlocks.POLISHED_ANORTHOSITE_STAIRS);
        polished_anorthosite_stone_pool.slab(AITBlocks.POLISHED_ANORTHOSITE_SLAB);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }

    public void registerDirectionalBlock(Block block) {
        directionalBlocksToRegister.add(block);
    }

    public void registerSimpleBlock(Block block) {
        simpleBlocksToRegister.add(block);
    }
}
