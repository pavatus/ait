package dev.amble.ait.mixin.client.sonic;

import dev.amble.ait.client.sonic.ParentedResourceFinder;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.resource.ResourceFinder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLoader.class)
public class SonicModelsFinderMixin {

    @Mutable
    @Shadow @Final public static ResourceFinder MODELS_FINDER;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        // FIXME: recursion happening here
        MODELS_FINDER = new ParentedResourceFinder(MODELS_FINDER, "models/item/sonic", ".json");
    }
}
