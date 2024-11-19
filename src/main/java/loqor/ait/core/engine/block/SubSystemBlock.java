package loqor.ait.core.engine.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.link.block.FluidLinkBlock;

public abstract class SubSystemBlock extends FluidLinkBlock {
    private final SubSystem.IdLike id;

    protected SubSystemBlock(Settings settings, SubSystem.IdLike system) {
        super(settings);

        this.id = system;
    }

    public SubSystem.IdLike getSystemId() {
        return this.id;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && !(world.isClient())) { // on break
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SubSystemBlockEntity be) {
                world.updateComparators(pos, this);
                be.onBroken(world, pos);
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof SubSystemBlockEntity be) {
                be.tick(world1, pos, state1);
            }
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);
        if (blockEntity instanceof SubSystemBlockEntity be)
            return be.useOn(state, world, player.isSneaking(), player, stack);

        return ActionResult.SUCCESS;
    }

    protected abstract BlockEntityType<? extends SubSystemBlockEntity> getType();
}
