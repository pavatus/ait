package dev.amble.ait.mixin.artron;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

import dev.amble.ait.core.events.ServerChunkEvents;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At("TAIL"))
    public void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerChunkEvents.TICK.invoker().onChunkTick((ServerWorld) (Object) this, chunk);
    }
}
