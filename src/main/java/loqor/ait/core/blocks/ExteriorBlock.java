package loqor.ait.core.blocks;

import loqor.ait.api.ICantBreak;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.ToIntFunction;

@SuppressWarnings("deprecation")
public class ExteriorBlock extends FallingBlock implements BlockEntityProvider, ICantBreak, Waterloggable {

	public static final int MAX_ROTATION_INDEX = RotationPropertyHelper.getMax();
	private static final int MAX_ROTATIONS = MAX_ROTATION_INDEX + 1;
	public static final IntProperty ROTATION = Properties.ROTATION;
	public static final IntProperty LEVEL_9 = Properties.LEVEL_15;
	public static final BooleanProperty WATERLOGGED;
	public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = state -> state.get(LEVEL_9);
	public static final VoxelShape LEDGE_DOOM = Block.createCuboidShape(0, 0, -3.5, 16, 1, 16);
	public static final VoxelShape CUBE_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 5.0, 16.0, 32.0, 16.0),
			Block.createCuboidShape(0, 0, -3.5, 16, 1, 16));
	public static final VoxelShape PORTALS_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 32.0, 16.0),
			Block.createCuboidShape(0, 0, -3.5, 16, 1, 16));
	public static final VoxelShape SIEGE_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

	public ExteriorBlock(Settings settings) {
		super(settings.nonOpaque());

		this.setDefaultState(this.stateManager.getDefaultState().with(ROTATION, 0).with(WATERLOGGED, false).with(LEVEL_9, 9));
	}

	public VoxelShape diagonalShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(-0.125, 0, -0.125, 0.875, 0.0625, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.25, 0.0625, 0.25, 0.875, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.3125, 0.0625, 0.1875, 0.875, 2, 0.25), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.1875, 0.0625, 0.3125, 0.25, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.125, 0.0625, 0.375, 0.1875, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.4375, 0.0625, 0.0625, 0.875, 2, 0.125), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.375, 0.0625, 0.125, 0.875, 2, 0.1875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.5625, 0.0625, -0.0625, 0.875, 2, 0), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.5, 0.0625, 0, 0.875, 2, 0.0625), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.625, 0.0625, -0.125, 0.875, 2, -0.0625), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0.0625, 0.0625, 0.4375, 0.125, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(0, 0.0625, 0.5, 0.0625, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(-0.0625, 0.0625, 0.5625, 0, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(-0.125, 0.0625, 0.625, -0.0625, 2, 0.875), BooleanBiFunction.OR);
		shape = VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(-0.3125, 0, -0.3125, 0.625, 0.0625, 0.625), BooleanBiFunction.OR);

		return shape;
	}
	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return 15;
	}

	@Override
	public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState().with(ROTATION, 0).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(LEVEL_9, 9);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, WATERLOGGED, LEVEL_9);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return AITItems.TARDIS_ITEM.getDefaultStack();
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return !(Boolean) state.get(WATERLOGGED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		VoxelShape normal = this.getNormalShape(state);

		if (!(blockEntity instanceof ExteriorBlockEntity exterior))
			return normal;

		Tardis tardis = exterior.tardis() != null
				? exterior.tardis().get() : null;

		if (tardis == null)
			return normal;

		if (tardis.siege().isActive())
			return SIEGE_SHAPE;

		TardisTravel.State travelState = tardis.travel().getState();
		if (travelState == TardisTravel.State.LANDED || exterior.getAlpha() > 0.75)
			return normal;

		if (DependencyChecker.hasPortals())
			return PORTALS_SHAPE;

		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		// todo move these to a reusable method

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof ExteriorBlockEntity exterior) || exterior.tardis().isEmpty())
			return getNormalShape(state);

		Tardis tardis = exterior.tardis().get();

		if (tardis.siege().isActive())
			return SIEGE_SHAPE;

		if (tardis.getExterior().getVariant().equals(ExteriorVariantRegistry.DOOM))
			return LEDGE_DOOM;

		// todo this better because disabling collisions looks bad, should instead only disable if near to the portal or if walking into the block from the door direction
		if (DependencyChecker.hasPortals())
			if (tardis.getDoor().isOpen() && tardis.getExterior().getVariant().hasPortals()) // for some reason this check totally murders fps ??
				return getLedgeShape(state);

		TardisTravel.State travelState = tardis.travel().getState();
		if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
			return getNormalShape(state);

		if (DependencyChecker.hasPortals()) {
			return PORTALS_SHAPE;
		}

		return VoxelShapes.empty();
	}

	public VoxelShape getNormalShape(BlockState state) {
		Optional<Direction> direction = RotationPropertyHelper.toDirection(state.get(ROTATION));

		if(direction.isEmpty()) {
			return rotateShape(Direction.NORTH, approximateDirection(state.get(ROTATION)), diagonalShape());
		} else {
			return rotateShape(Direction.NORTH, direction.get(), CUBE_NORTH_SHAPE);
		}
	}

	public Direction approximateDirection(int rotation) {
		return switch(rotation) {
			default -> Direction.NORTH;
			case 1, 2, 3 -> Direction.EAST;
			case 5, 6, 7 -> Direction.SOUTH;
			case 9, 10, 11 -> Direction.WEST;
		};
	}

	public VoxelShape getLedgeShape(BlockState state) {
		// fixme these wont have ledges probably
		Optional<Direction> direction = RotationPropertyHelper.toDirection(state.get(ROTATION));

		if(direction.isEmpty()) {
			return rotateShape(Direction.NORTH, approximateDirection(state.get(ROTATION)), diagonalShape());
		} else {
			return rotateShape(Direction.NORTH, direction.get(), CUBE_NORTH_SHAPE);
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof ExteriorBlockEntity exterior) || exterior.tardis().isEmpty())
			return getNormalShape(state);

		TardisTravel.State travelState = exterior.tardis().get().travel().getState();
		if (travelState == TardisTravel.State.LANDED || exterior.getAlpha() > 0.75)
			return getNormalShape(state);

		if (exterior.tardis().get().getExterior().getVariant().equals(ExteriorVariantRegistry.DOOM)) {
			return LEDGE_DOOM;
		}

		if (DependencyChecker.hasPortals()) {
			return PORTALS_SHAPE;
		}

		return VoxelShapes.empty();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof ExteriorBlockEntity exterior) {
			if (world.isClient()) {
				if (exterior.tardis().isEmpty()) {
					ClientTardisManager.getInstance().askTardis(new AbsoluteBlockPos(pos, world));
					return ActionResult.FAIL;
				}
				return ActionResult.SUCCESS;
			}

			if (exterior.tardis().isEmpty())
				return ActionResult.FAIL;

			exterior.useOn((ServerWorld) world, player.isSneaking(), player);
		}

		return ActionResult.CONSUME;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world.isClient())
			return;

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ExteriorBlockEntity exterior) {
			exterior.onEntityCollision(entity);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ExteriorBlockEntity(pos, state);
	}

	@Override
	public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
		return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		return (world1, blockPos, blockState, ticker) -> {
			if (ticker instanceof ExteriorBlockEntity exterior) {
				exterior.tick(world, blockPos, blockState, exterior);
			}
		};
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		super.onBroken(world, pos, state);

		if (!world.isClient()) {
			BlockEntity entity = world.getBlockEntity(pos);

			if (!(entity instanceof ExteriorBlockEntity))
				return;

			((ExteriorBlockEntity) entity).onBroken();
		}
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// this is called when the block is first placed, but we have a demat anim so nothing occurs.
		tryFall(state, world, pos);
	}

	public void tryFall(BlockState state, ServerWorld world, BlockPos pos) {
		if (!canFallThrough(world.getBlockState(pos.down())))
			return;

		Tardis tardis = this.findTardis(world, pos);

		if (tardis == null)
			return;

		boolean antigravs = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED);

		if (antigravs)
			return;

		if (tardis.travel().getState() != TardisTravel.State.LANDED)
			return;

		if (tardis.getExterior().getCategory().equals(CategoryRegistry.CORAL_GROWTH))
			return;

		FallingTardisEntity falling = FallingTardisEntity.spawnFromBlock(world, pos, state);

		if (state.get(WATERLOGGED)) {
			state.with(WATERLOGGED, false);
		}
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	private Tardis findTardis(ServerWorld world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior) {
			if (exterior.tardis().isEmpty())
				return null;

			return exterior.tardis().get();
		}

		return null;
	}

	public void onLanding(Tardis tardis, World world, BlockPos pos) {
		if (tardis == null)
			return;

		tardis.travel().setPosition(new AbsoluteBlockPos.Directed(pos, world, tardis.travel().getPosition().getRotation()));

		world.playSound(null, pos, AITSounds.LAND_THUD, SoundCategory.BLOCKS);
		((BiomeHandler) tardis.getHandlers().get(TardisComponent.Id.BIOME)).setBiome(tardis);

		for (BlockPos console : tardis.getDesktop().getConsolePos()) {
			FlightUtil.playSoundAtConsole(console, AITSounds.LAND_THUD, SoundCategory.BLOCKS);
		}

		PropertiesHandler.set(tardis, PropertiesHandler.IS_FALLING, false);
		DoorData.lockTardis(tardis.getDoor().previouslyLocked(), tardis, null, false);
	}

	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(16) == 0) {
			BlockPos blockPos = pos.down();
			if (canFallThrough(world.getBlockState(blockPos))) {
				ParticleUtil.spawnParticle(world, pos, random, ParticleTypes.TOTEM_OF_UNDYING);
			}
		}
	}

	@Override
	public void onTryBreak(World world, BlockPos pos, BlockState state) {

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
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(ROTATION, rotation.rotate(state.get(ROTATION), MAX_ROTATIONS));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(ROTATION, mirror.mirror(state.get(ROTATION), MAX_ROTATIONS));
	}

	static {
		WATERLOGGED = Properties.WATERLOGGED;
	}
}
