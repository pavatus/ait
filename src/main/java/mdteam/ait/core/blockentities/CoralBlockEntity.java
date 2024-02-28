package mdteam.ait.core.blockentities;

import mdteam.ait.core.AITBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CoralBlockEntity extends BlockEntity {
	public CoralBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, pos, state);
	}
}
