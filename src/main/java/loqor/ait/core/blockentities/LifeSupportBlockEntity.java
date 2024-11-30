package loqor.ait.core.blockentities;


import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlockEntity;

public class LifeSupportBlockEntity extends SubSystemBlockEntity {
    public LifeSupportBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.LIFE_SUPPORT_BLOCK_ENTITY_TYPE, pos, state, SubSystem.Id.LIFE_SUPPORT);
    }
}
