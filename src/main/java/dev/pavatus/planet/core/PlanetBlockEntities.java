package dev.pavatus.planet.core;

import dev.pavatus.lib.container.impl.BlockEntityContainer;
import dev.pavatus.planet.core.blockentities.OxygenatorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.entity.BlockEntityType;

public class PlanetBlockEntities implements BlockEntityContainer {

    public static BlockEntityType<OxygenatorBlockEntity> OXYGENATOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
            .create(OxygenatorBlockEntity::new, PlanetBlocks.OXYGENATOR_BLOCK).build();

}
