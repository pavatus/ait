package dev.amble.ait.core.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;

import dev.amble.ait.core.blockentities.FlagBlockEntity;
import dev.amble.ait.core.blocks.types.HorizontalDirectionalBlock;

@SuppressWarnings("deprecation")
public class FlagBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

    private static final VoxelShape POLE = VoxelShapes.cuboid(
            0.45,
            0,
            0.45,
            0.55,
            2,
            0.55
    );


    public FlagBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return POLE;
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FlagBlockEntity(pos, state);
    }
}
