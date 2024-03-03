package mdteam.ait.core;

import io.wispforest.owo.registration.annotations.AssignedName;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.entities.FallingTardisEntity;
import mdteam.ait.core.entities.TardisRealEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AITEntityTypes implements AutoRegistryContainer<EntityType<?>> {
	@AssignedName("control_entity")
	public static final EntityType<ConsoleControlEntity> CONTROL_ENTITY_TYPE = FabricEntityTypeBuilder.create(
			SpawnGroup.MISC, ConsoleControlEntity::new).dimensions(EntityDimensions.changing(0.125f, 0.125f)).build();

	@AssignedName("falling_tardis")
	public static final EntityType<FallingTardisEntity> FALLING_TARDIS_TYPE = FabricEntityTypeBuilder.create(
			SpawnGroup.MISC, FallingTardisEntity::new).dimensions(EntityDimensions.changing(0.98f, 0.98f)).build();

	@AssignedName("tardis_real")
	public static final EntityType<TardisRealEntity> TARDIS_REAL_ENTITY_TYPE = FabricEntityTypeBuilder.create(
			SpawnGroup.MISC, TardisRealEntity::new).dimensions(EntityDimensions.changing(0.98f, 1.96f)).build();

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
