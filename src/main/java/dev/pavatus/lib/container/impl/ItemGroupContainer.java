package dev.pavatus.lib.container.impl;

import dev.pavatus.lib.container.RegistryContainer;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface ItemGroupContainer extends RegistryContainer<ItemGroup> {

    @Override
    default Registry<ItemGroup> getRegistry() {
        return Registries.ITEM_GROUP;
    }

    @Override
    default Class<ItemGroup> getTargetClass() {
        return ItemGroup.class;
    }
}
