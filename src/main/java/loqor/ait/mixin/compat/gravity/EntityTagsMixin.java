package loqor.ait.mixin.compat.gravity;

import gravity_changer.EntityTags;
import loqor.ait.core.entities.BaseControlEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTags.class)
public class EntityTagsMixin {

    @Inject(method = "canChangeGravity", at = @At("RETURN"), cancellable = true)
    private static void canChangeGravity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof BaseControlEntity)
            cir.setReturnValue(false);
    }
}
