package loqor.ait.core.blocks.control;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.blockentities.control.ButtonControlBlockEntity;

public class ButtonControlBlock extends ControlBlock {

    public ButtonControlBlock(Settings settings) {
        super(settings);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ButtonControlBlockEntity(pos, state);
    }
}
