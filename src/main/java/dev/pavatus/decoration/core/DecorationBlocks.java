package dev.pavatus.decoration.core;

import dev.pavatus.decoration.DecorationModule;
import dev.pavatus.decoration.core.block.RoundelBlock;
import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.item.AItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;

import loqor.ait.datagen.datagen_providers.util.NoEnglish;

public class DecorationBlocks extends BlockContainer {
    @NoEnglish
    public static final Block MINT_ROUNDEL = new RoundelBlock(
            FabricBlockSettings.create().nonOpaque().strength(0.01F, 0.01F).pistonBehavior(PistonBehavior.DESTROY));
    @NoEnglish
    public static final Block WHITE_ROUNDEL = new RoundelBlock(
            FabricBlockSettings.create().nonOpaque().strength(0.01F, 0.01F).pistonBehavior(PistonBehavior.DESTROY));
    @NoEnglish
    public static final Block DARK_OAK_ROUNDEL = new RoundelBlock(
            FabricBlockSettings.create().nonOpaque().strength(0.01F, 0.01F).pistonBehavior(PistonBehavior.DESTROY));
    @NoEnglish
    public static final Block TARDIM_ROUNDEL = new RoundelBlock(
            FabricBlockSettings.create().nonOpaque().strength(0.01F, 0.01F).pistonBehavior(PistonBehavior.DESTROY));


    @Override
    public Item.Settings createBlockItemSettings(Block block) {
        return new AItemSettings().group(DecorationModule.instance().getItemGroup());
    }
}
