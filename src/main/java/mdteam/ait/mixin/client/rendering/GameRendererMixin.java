package mdteam.ait.mixin.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import mdteam.ait.client.util.ShaderUtils;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;updateWorldIcon()V"))
	public void render(CallbackInfo ci) {
		if (false) {
			RenderSystem.disableBlend();
			RenderSystem.disableDepthTest();
			RenderSystem.resetTextureMatrix();
			ShaderUtils.shader.render(ShaderUtils.client.getTickDelta());
		}
	}
}

