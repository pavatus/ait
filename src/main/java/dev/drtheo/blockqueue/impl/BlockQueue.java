package dev.drtheo.blockqueue.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import dev.drtheo.blockqueue.data.BlockData;
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
    public void place(ServerWorld world, int maxTime, int flags, Runnable finish) {
        StepQueue.scheduleSteps(() -> {
            BlockData block = this.pollBlock();

            if (block == null)
                return true;

            world.setBlockState(block.pos(), block.state(), flags);
            return false;
        }, 1, maxTime, finish);
    }

    protected abstract BlockData pollBlock();

    public static class Simple extends BlockQueue {

        private final Deque<BlockData> blocks = new ArrayDeque<>();

        @Override
        protected BlockData pollBlock() {
            return this.blocks.poll();
        }

        public void set(BlockPos pos, BlockState state) {
            this.blocks.add(new BlockData(state, pos));
        }
    }
}
