package mdteam.ait.core.blocks;

import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blockentities.DetectorBlockEntity;
import mdteam.ait.tardis.Tardis;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class DetectorBlock extends BlockWithEntity {
    public static final IntProperty POWER;
    public static final BooleanProperty INVERTED;
    protected static final VoxelShape SHAPE;

    public DetectorBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWER, 0).with(INVERTED, false));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWER);
    }

    private static void updateState(BlockState state, World world, BlockPos pos, Tardis tardis) {
        if(state.get(INVERTED)) {
            world.setBlockState(pos, state.with(POWER, tardis.inFlight() ? 15 : 0), Block.NOTIFY_ALL);
        } else {
            world.setBlockState(pos, state.with(POWER, tardis.hasPower() ? 15 : 0), Block.NOTIFY_ALL);
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

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DetectorBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, AITBlockEntityTypes.DETECTOR_BLOCK_ENTITY_TYPE, DetectorBlock::tick);
    }

    private static void tick(World world, BlockPos pos, BlockState state, DetectorBlockEntity blockEntity) {
        if (world.isClient() || blockEntity.findTardis().isEmpty()) return;
        updateState(state, world, pos, blockEntity.findTardis().get());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWER, INVERTED);
    }

    static {
        POWER = Properties.POWER;
        INVERTED = Properties.INVERTED;
        SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    }
}
