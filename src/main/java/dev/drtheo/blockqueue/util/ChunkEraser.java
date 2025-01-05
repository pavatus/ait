package dev.drtheo.blockqueue.util;

import dev.drtheo.blockqueue.impl.BlockQueue;
import dev.drtheo.blockqueue.impl.StepQueue;

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

    // TODO: use completablefutures instead
    public static void erase(Runnable finish, ServerWorld world, ChunkPos from, ChunkPos to, int flags, boolean loadChunks) {
        erase(finish, world, from.x, from.z, to.x, to.z, flags, loadChunks);
    }

    public static void erase(Runnable finish, ServerWorld world, int chunkX1, int chunkZ1, int chunkX2, int chunkZ2, int flags, boolean loadChunks) {
        BlockQueue.LOGGER.info("Erasing from ({},{}) to ({},{})",
                chunkX1, chunkZ1, chunkX2, chunkZ2);

        long start = System.currentTimeMillis();

        BlockQueue.Simple queue = new BlockQueue.Simple();
        BlockState state = Blocks.AIR.getDefaultState();

        int minX = Math.min(chunkX1, chunkX2);
        int maxX = Math.max(chunkX1, chunkX2);

        int minZ = Math.min(chunkZ1, chunkZ2);
        int maxZ = Math.max(chunkZ1, chunkZ2);

        var ctx = new Object() {
            int x = minX;
        };

        StepQueue.scheduleSteps(() -> {
            for (int z = minZ; z < maxZ; z++) {
                Chunk chunk = world.getChunk(ctx.x, z, ChunkStatus.EMPTY, loadChunks);

                if (chunk == null)
                    return false;

                BlockQueue.LOGGER.info("Processing chunk at ({},{})", ctx.x, z);

                for (int y = chunk.getSectionIndex(world.getBottomY()); y < chunk.getSectionIndex(world.getTopY()); y++) {
                    ChunkSection section = chunk.getSectionArray()[y];

                    if (!section.isEmpty())
                        markBlocks(queue, state, ctx.x, z, chunk.sectionIndexToCoord(y));
                }
            }

            ctx.x++;

            if (ctx.x >= maxX) {
                queue.place(world, 20, flags, () -> BlockQueue.LOGGER.info("Finished erasing in {}ms", System.currentTimeMillis() - start));
                return true;
            }

            return false;
        }, 1, 20, finish);
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
}
