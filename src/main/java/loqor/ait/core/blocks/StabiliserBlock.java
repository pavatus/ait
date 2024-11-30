package loqor.ait.core.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlock;
import loqor.ait.core.engine.block.SubSystemBlockEntity;

public class StabiliserBlock extends SubSystemBlock {
    public StabiliserBlock(Settings settings) {
        super(settings, SubSystem.Id.STABILISERS);
    }

    @Override
    protected BlockEntityType<? extends SubSystemBlockEntity> getType() {
        return AITBlockEntityTypes.STABILISER_BLOCK_ENTITY_TYPE;
    }

    public static class StabiliserBlockEntity extends SubSystemBlockEntity {
        public StabiliserBlockEntity(BlockPos pos, BlockState state) {
            super(AITBlockEntityTypes.STABILISER_BLOCK_ENTITY_TYPE, pos, state, SubSystem.Id.STABILISERS);
        }
    }
}
