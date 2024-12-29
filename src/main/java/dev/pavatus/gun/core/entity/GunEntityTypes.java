package dev.pavatus.gun.core.entity;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class GunEntityTypes implements AutoRegistryContainer<EntityType<?>>  {
    Identifier id = Identifier.of(AITMod.MOD_ID, "stazer_bolt_entity_type");
    RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);

    public static final EntityType<StaserBoltEntity> STASER_BOLT_ENTITY_TYPE = EntityType.Builder
            .<StaserBoltEntity>create(StaserBoltEntity::new, SpawnGroup.MISC)
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
