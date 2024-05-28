package loqor.ait.core.blocks;

import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.blocks.types.HorizontalDirectionalBlock;
import loqor.ait.core.AITBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class DoorBlock extends HorizontalDirectionalBlock implements BlockEntityProvider, Waterloggable {

	public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 12.1, 16.0, 32.0, 16.0);

	public static final BooleanProperty WATERLOGGED;

	public DoorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (world.getBlockEntity(pos) instanceof DoorBlockEntity door && door.findTardis().isPresent() && door.findTardis().get().siege().isActive())
			return VoxelShapes.empty();

		return rotateShape(Direction.NORTH, state.get(FACING), NORTH_SHAPE);
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

		int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.combine(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanBiFunction.OR));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		return buffer[0];
	}

	@Override
	public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient())
			return ActionResult.SUCCESS;

		if (world.getBlockEntity(pos) instanceof DoorBlockEntity door)
			door.useOn(world, player.isSneaking(), player);

		return ActionResult.CONSUME;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE ? DoorBlockEntity::tick : null;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world.isClient())
			return;

		Vec3d expansionBehind = new Vec3d(entity.prevX, entity.prevY, entity.prevZ).subtract(entity.getPos());
		Vec3d expansionForward = entity.getVelocity();

		Box entityBox = entity.getBoundingBox().stretch(
				expansionForward.multiply(1.2)).stretch(
						expansionBehind);

		Box doorShape = this.getOutlineShape(state, world, pos, ShapeContext.of(entity)).getBoundingBox().offset(pos);

		double insideBlockExpanded = 1.0E-7D;

		Box biggerEntityBox = entityBox.expand(insideBlockExpanded);
		Box biggerDoorShape = doorShape.expand(insideBlockExpanded);

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if(biggerEntityBox.intersects(biggerDoorShape)) {
			if (blockEntity instanceof DoorBlockEntity door)
				door.onEntityCollision(entity);
		}
		//if(Objects.equals(this.getOutlineShape(state, world, pos, ShapeContext.of(entity)).getClosestPointTo(entity.getPos()), Optional.of(new Vec3d(0, 0, 0)))) {
		//if (blockEntity instanceof DoorBlockEntity door)
		//	door.onEntityCollision(entity);
		//}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DoorBlockEntity(pos, state);
	}

	@Override
	public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
		return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return !(Boolean) state.get(WATERLOGGED);
	}

	static {
		WATERLOGGED = Properties.WATERLOGGED;
	}
}
