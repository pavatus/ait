package dev.pavatus.gun.core.entity;

import dev.pavatus.lib.container.impl.EntityContainer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class GunEntityTypes implements EntityContainer {

    public static final EntityType<StaserBoltEntity> STASER_BOLT_ENTITY_TYPE = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, StaserBoltEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build();
}
