package loqor.ait.mixin.networking;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerChunkManager.class)
public interface ServerChunkManagerAccessor {

    @Invoker("getChunkHolder")
    ChunkHolder ait$chunkHolder(long pos);
}
