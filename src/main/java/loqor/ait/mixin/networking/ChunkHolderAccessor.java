package loqor.ait.mixin.networking;

import net.minecraft.server.world.ChunkHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkHolder.class)
public interface ChunkHolderAccessor {

    @Accessor
    ChunkHolder.PlayersWatchingChunkProvider getPlayersWatchingChunkProvider();
}
