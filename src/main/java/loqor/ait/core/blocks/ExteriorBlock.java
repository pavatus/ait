package loqor.ait.core.blocks;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.api.ICantBreak;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.registry.CategoryRegistry;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.AbsoluteBlockPos;
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
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static loqor.ait.core.blocks.DoorBlock.rotateShape;

public class ExteriorBlock extends FallingBlock implements BlockEntityProvider, ICantBreak, Waterloggable {

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED;
	public static final VoxelShape LEDGE_DOOM = Block.createCuboidShape(0, 0, -3.5, 16, 1, 16);
	public static final VoxelShape CUBE_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 5.0, 16.0, 32.0, 16.0),
			Block.createCuboidShape(0, 0, -3.5, 16, 1, 16));
	public static final VoxelShape PORTALS_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 32.0, 16.0),
			Block.createCuboidShape(0, 0, -3.5, 16, 1, 16));
	public static final VoxelShape SIEGE_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

	public ExteriorBlock(Settings settings) {
		super(settings.nonOpaque());

		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
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
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	public static boolean isWaterlogged(BlockState state) {
		return state.get(Properties.WATERLOGGED);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
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
		if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).findTardis().isEmpty())
			return getNormalShape(state, world, pos);

		if (((ExteriorBlockEntity) blockEntity).findTardis().get().isSiegeMode())
			return SIEGE_SHAPE;

		TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).findTardis().get().getTravel().getState();
		if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
			return getNormalShape(state, world, pos);

		if (DependencyChecker.hasPortals()) {
			return PORTALS_SHAPE;
		}

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
		if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).findTardis().isEmpty())
			return getNormalShape(state, world, pos);

		Tardis tardis = ((ExteriorBlockEntity) blockEntity).findTardis().get();

		if (tardis.isSiegeMode())
			return SIEGE_SHAPE;
		if (tardis.getExterior().getVariant().equals(ExteriorVariantRegistry.DOOM)) {
			return LEDGE_DOOM;
		}
		// todo this better because disabling collisions looks bad, should instead only disable if near to the portal or if walking into the block from the door direction
		if (DependencyChecker.hasPortals())
			if (tardis.getDoor().isOpen() && ((ExteriorBlockEntity) blockEntity).findTardis().get().getExterior().getVariant().hasPortals()) // for some reason this check totally murders fps ??
				return getLedgeShape(state, world, pos);

		TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).findTardis().get().getTravel().getState();
		if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
			return getNormalShape(state, world, pos);

		if (DependencyChecker.hasPortals()) {
			return PORTALS_SHAPE;
		}

		return VoxelShapes.empty();
	}

	public VoxelShape getNormalShape(BlockState state, BlockView world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior && exterior.findTardis().isPresent() && exterior.findTardis().get().getExterior().getVariant().bounding(state.get(FACING)) != null)
			return exterior.findTardis().get().getExterior().getVariant().bounding(state.get(FACING));

		return rotateShape(Direction.NORTH, state.get(FACING), CUBE_NORTH_SHAPE);
	}

	public VoxelShape getLedgeShape(BlockState state, BlockView world, BlockPos pos) {
		// fixme these wont have ledges probably
		if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior && exterior.findTardis().isPresent() && exterior.findTardis().get().getExterior().getVariant().bounding(state.get(FACING)) != null)
			return exterior.findTardis().get().getExterior().getVariant().bounding(state.get(FACING));

		return rotateShape(Direction.NORTH, state.get(FACING), CUBE_NORTH_SHAPE);
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).findTardis().isEmpty())
			return getNormalShape(state, world, pos);

		TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).findTardis().get().getTravel().getState();
		if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
			return getNormalShape(state, world, pos);

		if (((ExteriorBlockEntity) blockEntity).findTardis().get().getExterior().getVariant().equals(ExteriorVariantRegistry.DOOM)) {
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
		if (blockEntity instanceof ExteriorBlockEntity exteriorBlockEntity) {
			if (world.isClient()) {
				if (exteriorBlockEntity.findTardis().isEmpty()) {
					ClientTardisManager.getInstance().askTardis(new AbsoluteBlockPos(pos, world));
					return ActionResult.FAIL;
				}
				return ActionResult.SUCCESS;
			}

			if (exteriorBlockEntity.findTardis().isEmpty()) return ActionResult.FAIL;
			exteriorBlockEntity.useOn((ServerWorld) world, player.isSneaking(), player);
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
		if (!canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= (world.getBottomY() + 1)) return;

		Tardis tardis = findTardis(world, pos);

		if (tardis == null) return;

		boolean antigravs = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED);

		if (antigravs) return;

		if (tardis.getTravel().getState() != TardisTravel.State.LANDED) return;

		if (tardis.getExterior().getCategory().equals(CategoryRegistry.CORAL_GROWTH)) return;

		FallingTardisEntity falling = FallingTardisEntity.spawnFromBlock(world, pos, state);

		if (state.get(WATERLOGGED)) {
			state.with(WATERLOGGED, false);
		}

		this.configureFallingTardis(falling, world, pos);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	private Tardis findTardis(ServerWorld world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior) {
			if (exterior.findTardis().isEmpty()) return null;
			return exterior.findTardis().get();
		}
		return null;
	}

	public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingTardisEntity falling) {
		Tardis tardis = falling.getTardis();

		if (tardis == null) return;
		tardis.getTravel().setPosition(new AbsoluteBlockPos.Directed(pos, world, tardis.getTravel().getPosition().getDirection()));

		world.playSound(null, pos, AITSounds.LAND_THUD, SoundCategory.BLOCKS);
		FlightUtil.playSoundAtConsole(tardis, AITSounds.LAND_THUD, SoundCategory.BLOCKS);

		PropertiesHandler.set(tardis, PropertiesHandler.IS_FALLING, false);
		DoorData.lockTardis(PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), tardis, null, false);
	}

	protected void configureFallingTardis(FallingTardisEntity entity, ServerWorld world, BlockPos pos) {
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

	static {
		WATERLOGGED = Properties.WATERLOGGED;
	}
}
