package dev.amble.ait.core.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.core.AITBlockEntityTypes;

public class SnowGlobeBlockEntity extends BlockEntity {
    public SnowGlobeBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.SNOW_GLOBE_BLOCK_ENTITY_TYPE, pos, state);
    }
}
