package dev.drtheo.blockqueue;

import java.util.ArrayDeque;
import java.util.Deque;

import dev.drtheo.blockqueue.api.Finishable;
import dev.drtheo.blockqueue.data.BlockData;
import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.blockqueue.util.StepUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class BlockQueue {

    public static final Logger LOGGER = LoggerFactory.getLogger("block-queue");

    /**
     * @param maxTime Max time (in ms) a single cycle can perform
     */
    public void place(@Nullable Finishable callback, ServerWorld world, TimeUnit unit, int period, int maxTime, int flags) {
        StepUtil.scheduleSteps(callback, () -> {
            BlockData block = this.pollBlock();

            if (block == null)
                return true;

            int blockFlags = block.flags() == -1 ? flags : block.flags();
            world.setBlockState(block.pos(), block.state(), blockFlags);
            return false;
        }, unit, period, maxTime);
    }

    protected abstract BlockData pollBlock();

    public static class Simple extends BlockQueue {

        private final Deque<BlockData> blocks = new ArrayDeque<>();

        @Override
        protected BlockData pollBlock() {
            return this.blocks.poll();
        }

        public void set(BlockPos pos, BlockState state) {
            this.set(pos, state, -1);
        }

        public void set(BlockPos pos, BlockState state, int flags) {
            this.blocks.add(new BlockData(state, pos, flags));
        }
    }
}
