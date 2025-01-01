package dev.pavatus.lib.container.impl;

import dev.pavatus.lib.container.RegistryContainer;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface BlockEntityContainer extends RegistryContainer<BlockEntityType<?>> {

    @Override
    default Class<BlockEntityType<?>> getTargetClass() {
        return RegistryContainer.conform(BlockEntityType.class);
    }

    @Override
    default Registry<BlockEntityType<?>> getRegistry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }
}
