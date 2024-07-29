package loqor.ait.mixin.networking;

import loqor.ait.api.tardis.TardisEvents;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Shadow protected abstract void sendChunkDataPackets(ServerPlayerEntity player, MutableObject<ChunkDataS2CPacket> cachedDataPacket, WorldChunk chunk);

    @Redirect(method = "sendWatchPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;sendChunkDataPackets(Lnet/minecraft/server/network/ServerPlayerEntity;Lorg/apache/commons/lang3/mutable/MutableObject;Lnet/minecraft/world/chunk/WorldChunk;)V"))
    protected void sendWatchPackets(
            ThreadedAnvilChunkStorage instance, ServerPlayerEntity player, MutableObject<ChunkDataS2CPacket> cachedDataPacket, WorldChunk chunk
    ) {
        TardisEvents.SYNC_TARDIS.invoker().sync(player, chunk.getPos());
        this.sendChunkDataPackets(player, cachedDataPacket, chunk);
    }

    @Redirect(method = "sendWatchPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendUnloadChunkPacket(Lnet/minecraft/util/math/ChunkPos;)V"))
    protected void sendWatchPackets(
            ServerPlayerEntity instance, ChunkPos chunkPos
    ) {
        TardisEvents.UNLOAD_TARDIS.invoker().unload(instance, chunkPos);
        instance.sendUnloadChunkPacket(chunkPos);
    }
}
