package dev.pavatus.christmas.core;

import dev.pavatus.christmas.ChristmasModule;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;

public class ChristmasBlocks implements BlockRegistryContainer {
    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return ChristmasModule.instance().createBlockItem(block, identifier);
    }
}
