package dev.pavatus.register.auto;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface PaintingRegistryContainer extends AutoRegistryContainer<PaintingVariant> {

    @Override
    default Registry<PaintingVariant> getRegistry() {
        return Registries.PAINTING_VARIANT;
    }

    @Override
    default Class<PaintingVariant> getTargetFieldType() {
        return PaintingVariant.class;
    }
}
