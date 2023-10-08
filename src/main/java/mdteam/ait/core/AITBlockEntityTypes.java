package mdteam.ait.core;

import mdteam.ait.core.blockentities.AITRadioBlockEntity;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.core.blockentities.DisplayConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AITBlockEntityTypes implements AutoRegistryContainer<BlockEntityType<?>> {

    public static final BlockEntityType<AITRadioBlockEntity> AIT_RADIO_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(
            AITRadioBlockEntity::new, AITBlocks.RADIO).build();

    public static final BlockEntityType<DisplayConsoleBlockEntity> DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(
            DisplayConsoleBlockEntity::new, AITBlocks.DISPLAY_CONSOLE).build();

    public static final BlockEntityType<ExteriorBlockEntity> EXTERIOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(
            ExteriorBlockEntity::new, AITBlocks.EXTERIOR_BLOCK).build();

    @Override
    public Registry<BlockEntityType<?>> getRegistry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<BlockEntityType<?>> getTargetFieldType() {
        return (Class<BlockEntityType<?>>) (Object) BlockEntityType.class;
    }
}
