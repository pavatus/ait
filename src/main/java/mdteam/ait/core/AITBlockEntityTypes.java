package mdteam.ait.core;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.core.blockentities.*;
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
    public static BlockEntityType<AITRadioBlockEntity> AIT_RADIO_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(AITRadioBlockEntity::new, AITBlocks.RADIO).build();
    public static BlockEntityType<MonitorBlockEntity> MONITOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(MonitorBlockEntity::new, AITBlocks.MONITOR_BLOCK).build();
    public static BlockEntityType<DetectorBlockEntity> DETECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(DetectorBlockEntity::new, AITBlocks.DETECTOR_BLOCK).build();
    public static BlockEntityType<ArtronCollectorBlockEntity> ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(ArtronCollectorBlockEntity::new, AITBlocks.ARTRON_COLLECTOR_BLOCK).build();

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
