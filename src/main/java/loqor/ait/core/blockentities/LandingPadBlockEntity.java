package loqor.ait.core.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlockEntityTypes;

public class LandingPadBlockEntity extends BlockEntity {

    public LandingPadBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.LANDING_PAD_BLOCK_ENTITY_TYPE, pos, state);
    }
}
