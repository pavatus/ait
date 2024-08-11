package loqor.ait.core.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import loqor.ait.core.blockentities.PlugBoardBlockEntity;
import loqor.ait.core.data.ShapeMap;
import loqor.ait.core.util.ShapeUtil;

@SuppressWarnings("deprecation")
public class PlugBoardBlock extends Block implements BlockEntityProvider {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = Properties.FACING;

    private static final VoxelShape BUTTON = ShapeUtil.rect(5, 4, 12.5, 1, 1, 1.5);
    private static final VoxelShape SHAPE = ShapeUtil.rect(4, 2, 14, 8, 12, 2);
    private static final ShapeMap FACING_TO_SHAPE;

    public static final Handle<Context>[] HANDLES = new Handle[12];

    static {
        List<VoxelShape> buttons = new ArrayList<>();
        buttons.addAll(side(0));
        buttons.addAll(side(5));

        VoxelShape all = VoxelShapes.union(SHAPE, buttons.toArray(new VoxelShape[0]));
        FACING_TO_SHAPE = ShapeUtil.rotations(Direction.NORTH, all).build();

        for (int i = 0; i < buttons.size(); i++) {
            VoxelShape button = buttons.get(i);
            final int index = i;

            HANDLES[i] = new Handle<>(ShapeUtil.rotations(Direction.NORTH, button).build(),
                    ctx -> ctx.entity.onClick(ctx.player, ctx.hand, index));
        }
    }

    private static List<VoxelShape> side(int y) {
        List<VoxelShape> result = new ArrayList<>(6);

        result.addAll(row(y));
        result.addAll(row(y + 2));
        return result;
    }

    private static List<VoxelShape> row(int y) {
        List<VoxelShape> elements = new ArrayList<>();
        double x = 1;

        for (int i = 0; i < 3; i++) {
            elements.add(button(x, y));
            x += 1.5;
        }

        return elements;
    }

    private static VoxelShape button(double x, double y) {
        return BUTTON.offset(x / 16, y / 16, 0);
    }

    public PlugBoardBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof PlugBoardBlockEntity casing))
            return ActionResult.FAIL;

        Context context = new Context(player, hand, casing);

        for (Handle<Context> handle : HANDLES) {
            handle.check(hit, state.get(FACING), context);
        }

        return ActionResult.SUCCESS;
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
    @Nullable public BlockState getPlacementState(ItemPlacementContext ctx) {
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
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos))
            return Blocks.AIR.getDefaultState();

        if (state.get(WATERLOGGED))
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
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

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlugBoardBlockEntity(pos, state);
    }

    public record Context(PlayerEntity player, Hand hand, PlugBoardBlockEntity entity) {
    }

    public record Handle<T>(ShapeMap shape, Consumer<T> runnable) {

        public void check(BlockHitResult hit, Direction direction, T t) {
            double mouseX = (hit.getPos().x * 16) - (hit.getBlockPos().getX() * 16);
            double mouseY = (hit.getPos().y * 16) - (hit.getBlockPos().getY() * 16);
            double mouseZ = (hit.getPos().z * 16) - (hit.getBlockPos().getZ() * 16);

            VoxelShape s = shape.get(direction);

            double minX = s.getMin(Direction.Axis.X);
            double maxX = s.getMax(Direction.Axis.X);
            double minY = s.getMin(Direction.Axis.Y);
            double maxY = s.getMax(Direction.Axis.Y);
            double minZ = s.getMin(Direction.Axis.Z);
            double maxZ = s.getMax(Direction.Axis.Z);

            boolean checkX = mouseX >= (minX * 16) && mouseX <= (maxX * 16);

            boolean checkY = mouseY >= (minY * 16) && mouseY <= (maxY * 16);

            boolean checkZ = mouseZ >= (minZ * 16) && mouseZ <= (maxZ * 16);

            if (checkX && checkY && checkZ)
                this.runnable.accept(t);
        }
    }
}
