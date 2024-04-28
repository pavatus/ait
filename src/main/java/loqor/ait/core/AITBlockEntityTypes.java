package loqor.ait.core;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import loqor.ait.core.blockentities.*;
import loqor.ait.core.blockentities.control.ButtonControlBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AITBlockEntityTypes implements AutoRegistryContainer<BlockEntityType<?>> {

    public static BlockEntityType<ExteriorBlockEntity> EXTERIOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(ExteriorBlockEntity::new, AITBlocks.EXTERIOR_BLOCK).build();
    public static BlockEntityType<DoorBlockEntity> DOOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(DoorBlockEntity::new, AITBlocks.DOOR_BLOCK).build();
    public static BlockEntityType<ConsoleBlockEntity> CONSOLE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(ConsoleBlockEntity::new, AITBlocks.CONSOLE).build();
    public static BlockEntityType<ConsoleGeneratorBlockEntity> CONSOLE_GENERATOR_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(ConsoleGeneratorBlockEntity::new, AITBlocks.CONSOLE_GENERATOR).build();
    public static BlockEntityType<CoralBlockEntity> CORAL_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(CoralBlockEntity::new, AITBlocks.CORAL_PLANT).build();
    public static BlockEntityType<MonitorBlockEntity> MONITOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(MonitorBlockEntity::new, AITBlocks.MONITOR_BLOCK).build();
    public static BlockEntityType<DetectorBlockEntity> DETECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(DetectorBlockEntity::new, AITBlocks.DETECTOR_BLOCK).build();
    public static BlockEntityType<ArtronCollectorBlockEntity> ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(ArtronCollectorBlockEntity::new, AITBlocks.ARTRON_COLLECTOR_BLOCK).build();
    public static BlockEntityType<PlaqueBlockEntity> PLAQUE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(PlaqueBlockEntity::new, AITBlocks.PLAQUE_BLOCK).build();
    public static BlockEntityType<ButtonControlBlockEntity> BUTTON_CONTROL_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(ButtonControlBlockEntity::new, AITBlocks.BUTTON_CONTROL_BLOCK).build();
    public static BlockEntityType<EngineBlockEntity> ENGINE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(EngineBlockEntity::new, AITBlocks.ENGINE_BLOCK).build();
    public static BlockEntityType<WallMonitorBlockEntity> WALL_MONITOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(WallMonitorBlockEntity::new, AITBlocks.WALL_MONITOR_BLOCK).build();
    public static BlockEntityType<EngineCoreBlockEntity> ENGINE_CORE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(EngineCoreBlockEntity::new, AITBlocks.ENGINE_CORE_BLOCK).build();
    public static BlockEntityType<MachineCasingBlockEntity> MACHINE_CASING_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(MachineCasingBlockEntity::new, AITBlocks.MACHINE_CASING).build();

    public static final BlockEntityType<AITRadioBlockEntity> AIT_RADIO_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(
            AITRadioBlockEntity::new, AITBlocks.RADIO).build();

    @Override
    public Registry<BlockEntityType<?>> getRegistry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<BlockEntityType<?>> getTargetFieldType() {
        return (Class <BlockEntityType<?>>) (Object) BlockEntityType.class;
    }
}
