package loqor.ait.core;

import io.wispforest.owo.registration.reflect.BlockEntityRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.entity.BlockEntityType;

import loqor.ait.core.blockentities.*;
import loqor.ait.core.blockentities.control.RedstoneControlBlockEntity;
import loqor.ait.core.engine.link.block.FluidLinkBlockEntity;

public class AITBlockEntityTypes implements BlockEntityRegistryContainer {

    public static BlockEntityType<ExteriorBlockEntity> EXTERIOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(ExteriorBlockEntity::new, AITBlocks.EXTERIOR_BLOCK).build();
    public static BlockEntityType<DoorBlockEntity> DOOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(DoorBlockEntity::new, AITBlocks.DOOR_BLOCK).build();
    public static BlockEntityType<ConsoleBlockEntity> CONSOLE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(ConsoleBlockEntity::new, AITBlocks.CONSOLE).build();
    public static BlockEntityType<ConsoleGeneratorBlockEntity> CONSOLE_GENERATOR_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(ConsoleGeneratorBlockEntity::new, AITBlocks.CONSOLE_GENERATOR).build();
    public static BlockEntityType<CoralBlockEntity> CORAL_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(CoralBlockEntity::new, AITBlocks.CORAL_PLANT).build();
    public static BlockEntityType<MonitorBlockEntity> MONITOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(MonitorBlockEntity::new, AITBlocks.MONITOR_BLOCK).build();
    public static BlockEntityType<DetectorBlockEntity> DETECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(DetectorBlockEntity::new, AITBlocks.DETECTOR_BLOCK).build();
    public static BlockEntityType<ArtronCollectorBlockEntity> ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(ArtronCollectorBlockEntity::new, AITBlocks.ARTRON_COLLECTOR_BLOCK).build();
    public static BlockEntityType<PlaqueBlockEntity> PLAQUE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(PlaqueBlockEntity::new, AITBlocks.PLAQUE_BLOCK).build();
    public static BlockEntityType<EngineBlockEntity> ENGINE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(EngineBlockEntity::new, AITBlocks.ENGINE_BLOCK).build();
    public static BlockEntityType<WallMonitorBlockEntity> WALL_MONITOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(WallMonitorBlockEntity::new, AITBlocks.WALL_MONITOR_BLOCK).build();
    public static BlockEntityType<EngineCoreBlockEntity> ENGINE_CORE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(EngineCoreBlockEntity::new, AITBlocks.ENGINE_CORE_BLOCK).build();
    public static BlockEntityType<MachineCasingBlockEntity> MACHINE_CASING_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(MachineCasingBlockEntity::new, AITBlocks.MACHINE_CASING).build();
    public static BlockEntityType<PlugBoardBlockEntity> PLUGBOARD_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(PlugBoardBlockEntity::new, AITBlocks.PLUGBOARD).build();
    public static BlockEntityType<FabricatorBlockEntity> FABRICATOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(FabricatorBlockEntity::new, AITBlocks.FABRICATOR).build();
    public static BlockEntityType<EnvironmentProjectorBlockEntity> ENVIRONMENT_PROJECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(EnvironmentProjectorBlockEntity::new, AITBlocks.ENVIRONMENT_PROJECTOR).build();
    public static BlockEntityType<WaypointBankBlockEntity> WAYPOINT_BANK_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(WaypointBankBlockEntity::new, AITBlocks.WAYPOINT_BANK).build();

    public static final BlockEntityType<AITRadioBlockEntity> AIT_RADIO_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(AITRadioBlockEntity::new, AITBlocks.RADIO).build();
    public static BlockEntityType<RedstoneControlBlockEntity> REDSTONE_CONTROL_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
            .create(RedstoneControlBlockEntity::new, AITBlocks.REDSTONE_CONTROL_BLOCK).build();
    public static BlockEntityType<FluidLinkBlockEntity> FLUID_LINK_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
            .create(FluidLinkBlockEntity::new, AITBlocks.FLUID_LINK).build();
}
