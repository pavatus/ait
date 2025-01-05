package dev.drtheo.blockqueue.util;

import dev.drtheo.blockqueue.ActionQueue;
import dev.drtheo.blockqueue.BlockQueue;
import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.blockqueue.data.Value;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;

public class ChunkEraser {

    private static ActionQueue erase(TimeUnit unit, int period, int maxTime, ServerWorld world, int chunkX1, int chunkZ1, int chunkX2, int chunkZ2, int flags, boolean loadChunks) {
        BlockQueue.LOGGER.info("Erasing from ({},{}) to ({},{})",
                chunkX1, chunkZ1, chunkX2, chunkZ2);

        BlockQueue.Simple queue = new BlockQueue.Simple();
        BlockState state = Blocks.AIR.getDefaultState();

        int minX = Math.min(chunkX1, chunkX2);
        int maxX = Math.max(chunkX1, chunkX2);

        int minZ = Math.min(chunkZ1, chunkZ2);
        int maxZ = Math.max(chunkZ1, chunkZ2);

        Value<Integer> x = new Value<>(minX);

        return new ActionQueue()
                .thenRun(f -> StepUtil.scheduleSteps(f, () -> {
                    for (int z = minZ; z < maxZ; z++) {
                        Chunk chunk = world.getChunk(x.value, z, ChunkStatus.EMPTY, loadChunks);

                        if (chunk == null)
                            return false;

                        BlockQueue.LOGGER.info("Processing chunk at ({},{})", x.value, z);

                        for (int y = chunk.getSectionIndex(world.getBottomY()); y < chunk.getSectionIndex(world.getTopY()); y++) {
                            ChunkSection section = chunk.getSectionArray()[y];

                            if (!section.isEmpty())
                                markBlocks(queue, state, x.value, z, chunk.sectionIndexToCoord(y));
                        }
                    }

                    x.value++;
                    return x.value >= maxX;
                    }, unit, period, maxTime))
                .thenRun(f -> queue.place(f, world, unit, period, maxTime, flags));
    }

    private static void markBlocks(BlockQueue.Simple queue, BlockState state, int chunkX, int chunkZ, int chunkY) {
        int startX = ChunkSectionPos.getBlockCoord(chunkX);
        int startZ = ChunkSectionPos.getBlockCoord(chunkZ);
        int startY = ChunkSectionPos.getBlockCoord(chunkY);

        int endX = startX + 16;
        int endZ = startZ + 16;
        int endY = startY + 16;

        BlockQueue.LOGGER.info("Marking blocks from [{},{},{}] to [{},{},{}]",
                startX, startY, startZ, endX, endY, endZ);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (int z = startZ; z < endZ; z++) {
                    queue.set(new BlockPos(x, y, z), state);
                }
            }
        }
    }

    public static class Builder {

        private TimeUnit unit = TimeUnit.TICKS;
        private int period = 1;
        private int maxTime = 20;

        private int flags = Block.FORCE_STATE;
        private boolean loadChunks = true;

        public Builder every(TimeUnit unit, int period) {
            this.unit = unit;
            this.period = period;
            return this;
        }

        public Builder withMaxTime(int maxTime) {
            this.maxTime = maxTime;
            return this;
        }

        public Builder withFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder loadChunks(boolean loadChunks) {
            this.loadChunks = loadChunks;
            return this;
        }

        public ActionQueue build(ServerWorld world, int x1, int z1, int x2, int z2) {
            return ChunkEraser.erase(this.unit, this.period, this.maxTime,
                    world, x1, z1, x2, z2, this.flags, this.loadChunks);
        }

        public ActionQueue build(ServerWorld world, ChunkPos from, ChunkPos to) {
            return this.build(world, from.x, from.z, to.x, to.z);
        }
    }
}
