package dev.amble.ait.core.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.core.AITBlockEntityTypes;

public class ToyotaSpinningRotorBlockEntity extends BlockEntity {
    public ToyotaSpinningRotorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.TOYOTA_SPINNING_ROTOR_ENTITY_TYPE, pos, state);
    }
}
