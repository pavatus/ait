package dev.pavatus.christmas.core;

import dev.pavatus.christmas.ChristmasModule;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class ChristmasBlocks {
    public static void init() {

    }
    private static <T extends Block> T register(T block, Identifier id, boolean withItem) {
        return Registry.register(Registries.BLOCK, id, block);
    }
    private static <T extends Block> T register(T block, String name, boolean withItem) {
        if (withItem) {
            ChristmasItems.register(createBlockItem(block, name), name);
        }

        return register(block, new Identifier(AITMod.MOD_ID, name), withItem);
    }

    public static BlockItem createBlockItem(Block block, String identifier) {
        return ChristmasModule.instance().createBlockItem(block, identifier);
    }
}
