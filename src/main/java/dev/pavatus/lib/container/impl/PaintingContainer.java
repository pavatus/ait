package dev.pavatus.lib.container.impl;

import dev.pavatus.lib.container.RegistryContainer;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface PaintingContainer extends RegistryContainer<PaintingVariant> {

    @Override
    default Class<PaintingVariant> getTargetClass() {
        return PaintingVariant.class;
    }

    @Override
    default Registry<PaintingVariant> getRegistry() {
        return Registries.PAINTING_VARIANT;
    }
}
