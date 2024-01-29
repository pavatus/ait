package mdteam.ait.mixin.server;

import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.managers.RiftChunkManager;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements RiftChunk {
    @Shadow public abstract ChunkPos getPos();

    @Override
    public Integer getArtronLevels() {
        return RiftChunkManager.getArtronLevels(getPos());
    }


    @Override
    public Boolean isRiftChunk() {
        RiftChunkManager.initRiftChunk(getPos()); // Ensure that no matter what we will make sure that the RiftChunk is initialized
        Chunk chunk = ((Chunk)(Object)this);
        if (!(chunk instanceof WorldChunk worldChunk)) return false;
        ServerWorld world = (ServerWorld) worldChunk.getWorld();
        if (TardisUtil.getTardisDimension() == world) return false;
        if (world == null) return false;
        return ChunkRandom.getSlimeRandom(getPos().x, getPos().z, world.getSeed(), 987234910L).nextInt(8) == 0;
    }

    @Override
    public void setArtronLevels(int artron) {
        RiftChunkManager.setArtronLevels(getPos(), artron);
    }

//    @Inject(method ="<init>", at=@At("TAIL"))
//    public void chunkInit(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry biomeRegistry, long inhabitedTime, ChunkSection[] sectionArray, BlendingData blendingData, CallbackInfo ci) {
//        if (isRiftChunk()) {
//            RiftChunkManager.initRiftChunk(getPos());
//        }
//    }
}
