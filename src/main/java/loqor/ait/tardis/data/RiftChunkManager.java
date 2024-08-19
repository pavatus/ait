package loqor.ait.tardis.data;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;

import loqor.ait.AITMod;
import loqor.ait.tardis.util.TardisUtil;

@SuppressWarnings("UnstableApiUsage")
public class RiftChunkManager {

    private static final AttachmentType<Double> ARTRON = AttachmentRegistry.createPersistent(
            new Identifier(AITMod.MOD_ID, "artron"), Codec.DOUBLE
    );

    private static final AttachmentType<Double> MAX_ARTRON = AttachmentRegistry.createPersistent(
            new Identifier(AITMod.MOD_ID, "max_artron"), Codec.DOUBLE
    );

    public static void init() {

    }

    private final ServerWorld world;

    public static RiftChunkManager getInstance(ServerWorld world) {
        return new RiftChunkManager(world);
    }

    private RiftChunkManager(ServerWorld world) {
        this.world = world;
    }

    public double getArtron(ChunkPos pos) {
        return this.world.getChunk(pos.x, pos.z).getAttached(ARTRON);
    }

    public void removeFuel(ChunkPos pos, double amount) {
        this.world.getChunk(pos.x, pos.z).modifyAttached(ARTRON, d -> d - amount);
    }

    public double getMaxArtron(ChunkPos pos) {
        return this.world.getChunk(pos.x, pos.z).getAttached(MAX_ARTRON);
    }

    public static boolean isRiftChunk(ChunkPos chunkPos) {
        return ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z,
                        TardisUtil.getOverworld().getSeed(), 987234910L
                ).nextInt(8) == 0;
    }

    public static boolean isRiftChunk(BlockPos pos) {
        return isRiftChunk(new ChunkPos(pos));
    }
}
