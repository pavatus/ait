package loqor.ait.core;
import io.wispforest.owo.registration.annotations.AssignedName;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import loqor.ait.core.entities.CobbledSnowballEntity;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.core.entities.GallifreyFallsPaintingEntity;

public class AITEntityTypes implements AutoRegistryContainer<EntityType<?>> {
    @AssignedName("control_entity")
    public static final EntityType<ConsoleControlEntity> CONTROL_ENTITY_TYPE = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, ConsoleControlEntity::new).dimensions(EntityDimensions.changing(0.125f, 0.125f))
            .build();

    @AssignedName("falling_tardis")
    public static final EntityType<FallingTardisEntity> FALLING_TARDIS_TYPE = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, FallingTardisEntity::new).dimensions(EntityDimensions.changing(0.98f, 0.98f))
            .build();

    public static final EntityType<GallifreyFallsPaintingEntity> GALLIFREY_FALLS_PAINTING_TYPE = FabricEntityTypeBuilder
            .<GallifreyFallsPaintingEntity>create(SpawnGroup.MISC, GallifreyFallsPaintingEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build();

    public static final EntityType<CobbledSnowballEntity> COBBLED_SNOWBALL_TYPE = FabricEntityTypeBuilder
            .<CobbledSnowballEntity>create(SpawnGroup.MISC, CobbledSnowballEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build();

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
