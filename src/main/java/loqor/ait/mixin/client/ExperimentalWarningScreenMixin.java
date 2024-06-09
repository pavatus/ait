package loqor.ait.mixin.client;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import loqor.ait.AITMod;
import net.minecraft.client.gui.screen.pack.ExperimentalWarningScreen;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ExperimentalWarningScreen.class)
public class ExperimentalWarningScreenMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Collection<ResourcePackProfile> enabledProfiles, BooleanConsumer callback, CallbackInfo ci) {
        if (!AITMod.AIT_CONFIG.SHOW_EXPERIMENTAL_WARNING())
            callback.accept(true);
    }
}
