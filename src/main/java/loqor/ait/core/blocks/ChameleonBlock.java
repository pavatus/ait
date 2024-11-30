package loqor.ait.core.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlock;
import loqor.ait.core.engine.block.SubSystemBlockEntity;

// lol funny name
public class ChameleonBlock extends SubSystemBlock {
    public ChameleonBlock(Settings settings) {
        super(settings, SubSystem.Id.CHAMELEON);
    }

    @Override
    protected BlockEntityType<? extends SubSystemBlockEntity> getType() {
        return AITBlockEntityTypes.CHAMELEON_BLOCK_ENTITY_TYPE;
    }

    public static class ChameleonBlockEntity extends SubSystemBlockEntity {
        public ChameleonBlockEntity(BlockPos pos, BlockState state) {
            super(AITBlockEntityTypes.CHAMELEON_BLOCK_ENTITY_TYPE, pos, state, SubSystem.Id.CHAMELEON);
        }
    }
}
