package mdteam.ait.core.blocks;

import mdteam.ait.core.blockentities.ArtronCollectorBlockEntity;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtronCollectorBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

	protected static final VoxelShape Y_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 29.0, 10.0);

	public ArtronCollectorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Y_SHAPE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Y_SHAPE;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		return (world1, blockPos, blockState, ticker) -> {
			if (ticker instanceof ArtronCollectorBlockEntity collector) {
				collector.tick(world, blockPos, blockState, collector);
			}
		};
	}


	@Override
	public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ArtronCollectorBlockEntity(pos, state);
	}

	@Override
	public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
		return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ArtronCollectorBlockEntity artronCollectorBlockEntity)
			artronCollectorBlockEntity.useOn(world, player.isSneaking(), player);

		return ActionResult.SUCCESS;
	}
}
