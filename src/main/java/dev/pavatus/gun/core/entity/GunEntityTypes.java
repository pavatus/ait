package dev.pavatus.gun.core.entity;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GunEntityTypes implements AutoRegistryContainer<EntityType<?>>  {
    public static final EntityType<StaserBoltEntity> STASER_BOLT_ENTITY_TYPE = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, StaserBoltEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build();

    @Override
    public Registry<EntityType<?>> getRegistry() {
        return Registries.ENTITY_TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<EntityType<?>> getTargetFieldType() {
        return (Class<EntityType<?>>) (Object) EntityType.class;
    }
}
