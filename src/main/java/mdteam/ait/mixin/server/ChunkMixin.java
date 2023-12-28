package mdteam.ait.mixin.server;

import mdteam.ait.core.interfaces.RiftChunk;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.BlendingData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements RiftChunk {
    @Shadow public abstract ChunkPos getPos();

    @Unique
    private Integer artron_level = 0;
    @Override
    public Integer getArtronLevels() {
        return artron_level;
    }

    @Override
    public Boolean consumeArtron(int artron) {
        if (artron_level == 0) return false;
        if (artron_level - artron < 0) {
            return false;
        }
        artron_level -= artron;
        return true;
    }

    @Override
    public Boolean isRiftChunk() {
        Chunk chunk = ((Chunk)(Object)this);
        if (!(chunk instanceof WorldChunk)) return false;
        WorldChunk worldChunk = (WorldChunk) chunk;
        ChunkPos chunkPos = worldChunk.getPos();
        ServerWorld world = (ServerWorld) worldChunk.getWorld();
        if (world == null) return false;
        return ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z, world.getSeed(), 987234910L).nextInt(8) == 0;
    }

    @Inject(method ="<init>", at=@At("TAIL"))
    public void chunkInit(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry biomeRegistry, long inhabitedTime, ChunkSection[] sectionArray, BlendingData blendingData, CallbackInfo ci) {
        if (isRiftChunk()) {
            Random random = new Random();
            artron_level = random.nextInt(100, 800);
        } else {
            artron_level = 0;
        }
    }
}
