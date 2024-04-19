package loqor.ait.core.blocks.control;

import loqor.ait.core.AITItems;
import loqor.ait.core.blockentities.control.ButtonControlBlockEntity;
import loqor.ait.core.item.control.ControlBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ButtonControlBlock extends ControlBlock {
	public ButtonControlBlock(Settings settings) {
		super(settings);
	}

	/*@Override
	protected ControlBlockItem getItem() {
		return (ControlBlockItem) AITItems.BUTTON_CONTROL;
	}*/

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ButtonControlBlockEntity(pos, state);
	}
}
