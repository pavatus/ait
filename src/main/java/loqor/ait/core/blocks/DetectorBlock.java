package loqor.ait.core.blocks;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.blockentities.DetectorBlockEntity;
import loqor.ait.tardis.Tardis;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class DetectorBlock extends WallMountedBlock implements BlockEntityProvider {
	public static final BooleanProperty POWERED;
	public static final BooleanProperty INVERTED;
	public static final IntProperty POWER;
	protected static final VoxelShape NORTH_WALL_SHAPE;
	protected static final VoxelShape SOUTH_WALL_SHAPE;
	protected static final VoxelShape WEST_WALL_SHAPE;
	protected static final VoxelShape EAST_WALL_SHAPE;
	protected static final VoxelShape FLOOR_Z_AXIS_SHAPE;
	protected static final VoxelShape FLOOR_X_AXIS_SHAPE;
	protected static final VoxelShape CEILING_Z_AXIS_SHAPE;
	protected static final VoxelShape CEILING_X_AXIS_SHAPE;

	public DetectorBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(INVERTED, false).with(POWER, 0).with(POWERED, false).with(FACE, WallMountLocation.WALL));
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(FACE)) {
			case FLOOR:
				switch (state.get(FACING).getAxis()) {
					case X:
						return FLOOR_X_AXIS_SHAPE;
					case Z:
					default:
						return FLOOR_Z_AXIS_SHAPE;
				}
			case WALL:
				switch (state.get(FACING)) {
					case EAST:
						return EAST_WALL_SHAPE;
					case WEST:
						return WEST_WALL_SHAPE;
					case SOUTH:
						return SOUTH_WALL_SHAPE;
					case NORTH:
					default:
						return NORTH_WALL_SHAPE;
				}
			case CEILING:
			default:
				switch (state.get(FACING).getAxis()) {
					case X:
						return CEILING_X_AXIS_SHAPE;
					case Z:
					default:
						return CEILING_Z_AXIS_SHAPE;
				}
		}
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.canModifyBlocks()) {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				BlockState blockState = state.cycle(INVERTED);
				world.setBlockState(pos, blockState, Block.NO_REDRAW);
				world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
				return ActionResult.CONSUME;
			}
		} else {
			return super.onUse(state, world, pos, player, hand, hit);
		}
	}

	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!moved && !state.isOf(newState.getBlock())) {
			if (state.get(POWERED)) {
				this.updateNeighbors(state, world, pos);
			}

			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWER);
	}

	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	private void updateNeighbors(BlockState state, World world, BlockPos pos) {
		world.updateNeighborsAlways(pos, this);
		world.updateNeighborsAlways(pos.offset(getDirection(state).getOpposite()), this);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACE, FACING, POWERED, INVERTED, POWER);
	}

	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, AITBlockEntityTypes.DETECTOR_BLOCK_ENTITY_TYPE, DetectorBlock::tick);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}

	private static void updateState(BlockState state, World world, BlockPos pos, Tardis tardis) {
		if (state.get(INVERTED)) {
			world.setBlockState(pos, state.with(POWER, tardis.inFlight() ? 15 : 0).with(POWERED, true), Block.NOTIFY_ALL);
		} else {
			world.setBlockState(pos, state.with(POWER, tardis.engine().hasPower() ? 15 : 0).with(POWERED, true), Block.NOTIFY_ALL);
		}
	}

	private static void tick(World world, BlockPos pos, BlockState state, DetectorBlockEntity blockEntity) {
		if (world.isClient() || blockEntity.findTardis().isEmpty()) return;
		updateState(state, world, pos, blockEntity.findTardis().get());
	}

	static {
		POWERED = Properties.POWERED;
		POWER = Properties.POWER;
		INVERTED = Properties.INVERTED;
		NORTH_WALL_SHAPE = Block.createCuboidShape(5.0, 4.0, 10.0, 11.0, 12.0, 16.0);
		SOUTH_WALL_SHAPE = Block.createCuboidShape(5.0, 4.0, 0.0, 11.0, 12.0, 6.0);
		WEST_WALL_SHAPE = Block.createCuboidShape(10.0, 4.0, 5.0, 16.0, 12.0, 11.0);
		EAST_WALL_SHAPE = Block.createCuboidShape(0.0, 4.0, 5.0, 6.0, 12.0, 11.0);
		FLOOR_Z_AXIS_SHAPE = Block.createCuboidShape(5.0, 0.0, 4.0, 11.0, 6.0, 12.0);
		FLOOR_X_AXIS_SHAPE = Block.createCuboidShape(4.0, 0.0, 5.0, 12.0, 6.0, 11.0);
		CEILING_Z_AXIS_SHAPE = Block.createCuboidShape(5.0, 10.0, 4.0, 11.0, 16.0, 12.0);
		CEILING_X_AXIS_SHAPE = Block.createCuboidShape(4.0, 10.0, 5.0, 12.0, 16.0, 11.0);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DetectorBlockEntity(pos, state);
	}
}
