package mdteam.ait.core;

import com.neptunedevelopmentteam.neptunelib.core.init_handlers.CustomName;
import com.neptunedevelopmentteam.neptunelib.core.init_handlers.NeptuneEntityInit;
import io.wispforest.owo.registration.annotations.AssignedName;
import io.wispforest.owo.registration.reflect.EntityRegistryContainer;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.entities.FallingTardisEntity;
import mdteam.ait.core.entities.TardisRealEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class AITEntityTypes implements NeptuneEntityInit {
    @CustomName("control_entity")
    public static final EntityType<ConsoleControlEntity> CONTROL_ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.MISC, ConsoleControlEntity::new).dimensions(EntityDimensions.changing(0.125f, 0.125f)).build();

    @CustomName("falling_tardis")
    public static final EntityType<FallingTardisEntity> FALLING_TARDIS_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.MISC, FallingTardisEntity::new).dimensions(EntityDimensions.changing(0.98f, 0.98f)).build();

    @CustomName("tardis_real")
    public static final EntityType<TardisRealEntity> TARDIS_REAL_ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.MISC, TardisRealEntity::new).dimensions(EntityDimensions.changing(0.98f, 0.98f)).build();
}
