package mdteam.ait.core.blockentities.control;

import mdteam.ait.core.AITBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ButtonControlBlockEntity extends ControlBlockEntity {
	public ButtonControlBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.BUTTON_CONTROL_BLOCK_ENTITY, pos, state);
	}
}
