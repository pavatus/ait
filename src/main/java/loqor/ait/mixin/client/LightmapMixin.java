package loqor.ait.mixin.client;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class LightmapMixin {
	@Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
	private static void ait_changeBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> ci) {
		// if (MinecraftClient.getInstance().player.getWorld().getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
		//     ci.setReturnValue(ClientLightUtil.getBrightnessForInterior(lightLevel));
		// }
	}
}
