package dev.amble.ait.core.blocks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import dev.amble.ait.core.AITBlockEntityTypes;
import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.AITTags;
import dev.amble.ait.core.engine.link.block.DirectionalFluidLinkBlock;
import dev.amble.ait.core.engine.link.block.FluidLinkBlockEntity;

public class PowerConverterBlock extends DirectionalFluidLinkBlock {

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

            be.source().addLevel(175);
            stack.decrement(1);
            world.playSound(null, pos, AITSounds.POWER_CONVERT, SoundCategory.BLOCKS, 1.0F, 1.0F);

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntity(pos, state);
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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        tooltip.add(Text.translatable("tooltip.ait.power_converter").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
    }
}
