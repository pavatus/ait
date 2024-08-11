package loqor.ait.mixin.networking;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;

@Mixin(ServerChunkManager.class)
public interface ServerChunkManagerAccessor {

    @Accessor
    ThreadedAnvilChunkStorage getThreadedAnvilChunkStorage();
}
