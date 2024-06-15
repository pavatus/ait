package loqor.ait.core.util;

import loqor.ait.core.data.DirectedGlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.GlobalPos;

public class ForcedChunkUtil {
	
	public static void keepChunkLoaded(ServerWorld world, BlockPos pos) {
		ChunkPos chunk = new ChunkPos(pos);
		world.setChunkForced(chunk.x, chunk.z, true);
	}

	public static void stopForceLoading(ServerWorld world, BlockPos pos) {
		ChunkPos chunk = new ChunkPos(pos);
		world.setChunkForced(chunk.x, chunk.z, false);
	}

	public static boolean isChunkForced(ServerWorld world, BlockPos pos) {
		ChunkPos target = new ChunkPos(pos);
		ChunkPos temp;

		for (Long i : world.getForcedChunks()) {
			temp = new ChunkPos(i);
			if (temp.equals(target)) return true;
		}

		return false;
	}
}
