package loqor.ait.mixin.client.experimental_screen;

import com.mojang.serialization.Lifecycle;
import loqor.ait.AITMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IntegratedServerLoader.class)
public class WorldOpenFlowsMixin {

	@ModifyVariable(method = "start(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;ZZ)V", at = @At("STORE"), ordinal = 3)
	public boolean loadLevel_bl2(boolean canShowBackupPrompt) {
		if (!AITMod.AIT_CONFIG.SHOW_EXPERIMENTAL_WARNING())
			return false;

		return canShowBackupPrompt;
	}

	@Inject(method = "tryLoad", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/serialization/Lifecycle;experimental()Lcom/mojang/serialization/Lifecycle;", remap = false), cancellable = true)
	private static void confirmWorldCreation(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, boolean bypassWarnings, CallbackInfo ci) {
		if (!AITMod.AIT_CONFIG.SHOW_EXPERIMENTAL_WARNING()) {
			loader.run();
			ci.cancel();
		}
	}
}