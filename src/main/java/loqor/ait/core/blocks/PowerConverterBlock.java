package loqor.ait.core.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.AITTags;
import loqor.ait.core.engine.link.block.FluidLinkBlock;
import loqor.ait.core.engine.link.block.FluidLinkBlockEntity;

public class PowerConverterBlock extends FluidLinkBlock {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape Y_SHAPE = Block.createCuboidShape(
            4.0,
            0.0,
            2.5,
            12.0,
            32.0,
            13.5
    );


    public PowerConverterBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
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
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.getBlockEntity(pos) instanceof FluidLinkBlockEntity be) {
            if (world.isClient()) return ActionResult.SUCCESS;
            if (!(be.isPowered())) return ActionResult.FAIL;
            if (!stack.isIn(AITTags.Items.IS_TARDIS_FUEL)) return ActionResult.FAIL;

            be.source().addLevel(10);
            stack.decrement(1);
            world.playSound(null, pos, AITSounds.BWEEP, SoundCategory.BLOCKS, 1.0F, 1.0F);

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntity(pos, state);
    }

    @Nullable @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public static class BlockEntity extends FluidLinkBlockEntity {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(AITBlockEntityTypes.POWER_CONVERTER_BLOCK_TYPE, pos, state);
        }
    }
}
