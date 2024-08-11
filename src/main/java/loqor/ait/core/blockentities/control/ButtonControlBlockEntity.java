package loqor.ait.core.blockentities.control;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlockEntityTypes;

public class ButtonControlBlockEntity extends ControlBlockEntity {

    public ButtonControlBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.BUTTON_CONTROL_BLOCK_ENTITY, pos, state);
    }
}
