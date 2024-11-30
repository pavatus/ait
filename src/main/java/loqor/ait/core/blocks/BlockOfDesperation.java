package loqor.ait.core.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlock;
import loqor.ait.core.engine.block.SubSystemBlockEntity;

// lol funny name
public class BlockOfDesperation extends SubSystemBlock {
    public BlockOfDesperation(Settings settings) {
        super(settings, SubSystem.Id.DESPERATION);
    }

    @Override
    protected BlockEntityType<? extends SubSystemBlockEntity> getType() {
        return null;
    }

    public static class DesperationBlockEntity extends SubSystemBlockEntity {
        public DesperationBlockEntity(BlockPos pos, BlockState state) {
            super(AITBlockEntityTypes.DESPERATION_BLOCK_ENTITY_TYPE, pos, state, SubSystem.Id.DESPERATION);
        }
    }
}
