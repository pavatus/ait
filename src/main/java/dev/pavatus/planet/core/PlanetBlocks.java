package dev.pavatus.planet.core;

import dev.pavatus.planet.PlanetModule;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;

import loqor.ait.datagen.datagen_providers.util.NoBlockDrop;

public class PlanetBlocks implements BlockRegistryContainer {
    // Planetary Blocks

    // Mars

    // Martian Stone
    public static final Block MARTIAN_SAND = new Block(
            AbstractBlock.Settings.copy(Blocks.SAND));

    public static final Block MARTIAN_STONE = new Block(
            AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block MARTIAN_STONE_WALL = new WallBlock(
            AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block MARTIAN_STONE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block MARTIAN_STONE_STAIRS = new StairsBlock(
            MARTIAN_STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block MARTIAN_STONE_BUTTON = new ButtonBlock(
            AbstractBlock.Settings.copy(Blocks.STONE_BUTTON), BlockSetType.STONE, 10, false);


    public static final Block MARTIAN_STONE_PRESSURE_PLATE  = new PressurePlateBlock(
            PressurePlateBlock.ActivationRule.EVERYTHING, AbstractBlock.Settings.copy(Blocks.STONE_PRESSURE_PLATE),BlockSetType.STONE);

    // Martian Cobblestone


    public static final Block MARTIAN_COBBLESTONE = new Block(
            AbstractBlock.Settings.copy(Blocks.COBBLESTONE));


    public static final Block MARTIAN_COBBLESTONE_WALL = new WallBlock(
            AbstractBlock.Settings.copy(Blocks.COBBLESTONE_WALL));


    @NoBlockDrop
    public static final Block MARTIAN_COBBLESTONE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.COBBLESTONE_SLAB));


    public static final Block MARTIAN_COBBLESTONE_STAIRS = new StairsBlock(
            MARTIAN_COBBLESTONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.COBBLESTONE_STAIRS));


    public static final Block MARTIAN_PILLAR = new PillarBlock(
            AbstractBlock.Settings.copy(Blocks.QUARTZ_PILLAR));


    public static final Block CHISELED_MARTIAN_STONE = new Block(
            AbstractBlock.Settings.copy(Blocks.CHISELED_STONE_BRICKS));


    public static final Block CRACKED_MARTIAN_BRICKS = new Block(
            AbstractBlock.Settings.copy(Blocks.CRACKED_STONE_BRICKS));

    // Martian Bricks


    public static final Block MARTIAN_BRICKS = new Block(
            AbstractBlock.Settings.copy(Blocks.STONE_BRICKS));


    public static final Block MARTIAN_BRICK_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.STONE_BRICK_SLAB));


    public static final Block MARTIAN_BRICK_STAIRS = new StairsBlock(
            MARTIAN_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE_BRICK_STAIRS));


    public static final Block MARTIAN_BRICK_WALL = new WallBlock(
            AbstractBlock.Settings.copy(Blocks.STONE_BRICK_WALL));

    // Polished Martian Stone


    public static final Block POLISHED_MARTIAN_STONE = new Block(
            AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));


    public static final Block POLISHED_MARTIAN_STONE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE_SLAB));


    public static final Block POLISHED_MARTIAN_STONE_STAIRS = new StairsBlock(
            POLISHED_MARTIAN_STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE_STAIRS));


    // Smooth Martian Stone


    public static final Block SMOOTH_MARTIAN_STONE = new Block(
            AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));


    @NoBlockDrop
    public static final Block SMOOTH_MARTIAN_STONE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE_SLAB));

    // Moon

    // Anorthosite

    public static final Block REGOLITH = new Block(
            AbstractBlock.Settings.copy(Blocks.SAND));

    public static final Block ANORTHOSITE = new Block(
            AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block ANORTHOSITE_WALL = new WallBlock(
            AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block ANORTHOSITE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.STONE));


    public static final Block ANORTHOSITE_STAIRS = new StairsBlock(
            ANORTHOSITE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE));



    public static final Block ANORTHOSITE_PILLAR = new PillarBlock(
            AbstractBlock.Settings.copy(Blocks.QUARTZ_PILLAR));


    public static final Block CHISELED_ANORTHOSITE = new Block(
            AbstractBlock.Settings.copy(Blocks.CHISELED_STONE_BRICKS));


    public static final Block CRACKED_ANORTHOSITE_BRICKS = new Block(
            AbstractBlock.Settings.copy(Blocks.CRACKED_STONE_BRICKS));

    // Anorthosite Bricks


    public static final Block ANORTHOSITE_BRICKS = new Block(
            AbstractBlock.Settings.copy(Blocks.STONE_BRICKS));


    @NoBlockDrop
    public static final Block ANORTHOSITE_BRICK_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.STONE_BRICK_SLAB));


    public static final Block ANORTHOSITE_BRICK_STAIRS = new StairsBlock(
            ANORTHOSITE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE_BRICK_STAIRS));


    public static final Block ANORTHOSITE_BRICK_WALL = new WallBlock(
            AbstractBlock.Settings.copy(Blocks.STONE_BRICK_WALL));

    // Polished Anorthosite Stone


    public static final Block POLISHED_ANORTHOSITE = new Block(
            AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));


    public static final Block POLISHED_ANORTHOSITE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE_SLAB));


    public static final Block POLISHED_ANORTHOSITE_STAIRS = new StairsBlock(
        POLISHED_ANORTHOSITE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE_STAIRS));


    // Smooth Anorthosite Stone


    public static final Block SMOOTH_ANORTHOSITE = new Block(
            AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));


    @NoBlockDrop
    public static final Block SMOOTH_ANORTHOSITE_SLAB = new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE_SLAB));

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return PlanetModule.instance().createBlockItem(block, identifier);
    }
}
