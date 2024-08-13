package loqor.ait.mixin.server;

import loqor.ait.core.events.WorldSaveEvent;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "saveLevel", at = @At("HEAD"))
    private void saveLevel(CallbackInfo ci) {
        WorldSaveEvent.EVENT.invoker().onWorldSave((ServerWorld) (Object) this);
    }
}
