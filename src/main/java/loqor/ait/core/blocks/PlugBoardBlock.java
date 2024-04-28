package loqor.ait.core.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class PlugBoardBlock extends Block {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.createCuboidShape(0.0, 4.5, 14.0, 16.0, 12.5, 16.0),
            Direction.SOUTH, Block.createCuboidShape(0.0, 4.5, 0.0, 16.0, 12.5, 2.0),
            Direction.EAST, Block.createCuboidShape(0.0, 4.5, 0.0, 2.0, 12.5, 16.0),
            Direction.WEST, Block.createCuboidShape(14.0, 4.5, 0.0, 16.0, 12.5, 16.0))
    );

    public PlugBoardBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FACING_TO_SHAPE.get(state.get(FACING));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid();
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        World worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (!(blockState = blockState.with(FACING, direction.getOpposite())).canPlaceAt(worldView, blockPos))
                continue;

            return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
        }

        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos))
            return Blocks.AIR.getDefaultState();

        if (state.get(WATERLOGGED))
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public float getRotationDegrees(BlockState state) {
        return state.get(FACING).asRotation();
    }

    public Vec3d getCenter(BlockState state) {
        VoxelShape voxelShape = FACING_TO_SHAPE.get(state.get(FACING));
        return voxelShape.getBoundingBox().getCenter();
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED))
            return Fluids.WATER.getStill(false);

        return super.getFluidState(state);
    }

    record Handle(Function<Direction, VoxelShape> shape) {

        public boolean check(BlockHitResult hit, BlockState state) {
            double mouseX = (hit.getPos().x * 16) - (hit.getBlockPos().getX() * 16);
            double mouseY = (hit.getPos().y * 16) - (hit.getBlockPos().getY() * 16);
            double mouseZ = (hit.getPos().z * 16) - (hit.getBlockPos().getZ() * 16);

            VoxelShape s = shape.apply(state.get(FACING));

            double minX = s.getMin(Direction.Axis.X);
            double maxX = s.getMax(Direction.Axis.X);
            double minY = s.getMin(Direction.Axis.Y);
            double maxY = s.getMax(Direction.Axis.Y);
            double minZ = s.getMin(Direction.Axis.Z);
            double maxZ = s.getMax(Direction.Axis.Z);

            // DOWN: >= 0 <= 0 + 2
            // UP: >= 16 <= 18
            //
            boolean checkX = mouseX >= (minX * 16)
                    && mouseX <= (maxX * 16);

            boolean checkY = mouseY >= (minY * 16)
                    && mouseY <= (maxY * 16);

            boolean checkZ = mouseZ >= (minZ * 16)
                    && mouseZ <= (maxZ * 16);

            return checkX && checkY && checkZ;
        }

    }
}
