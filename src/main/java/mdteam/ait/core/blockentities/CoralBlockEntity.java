package mdteam.ait.core.blockentities;

import com.neptunedevelopmentteam.neptunelib.core.util.NeptuneUtil;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CoralBlockEntity extends BlockEntity {
    public CoralBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, pos, state);
    }
}
