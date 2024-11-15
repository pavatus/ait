package loqor.ait.core.engine.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
}
