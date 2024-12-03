package loqor.ait.mixin.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.MathHelper;

import loqor.ait.client.util.ShaderUtils;
import loqor.ait.core.item.BaseGunItem;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;updateWorldIcon()V"))
    public void ait$render(CallbackInfo ci) {
        if (false) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            ShaderUtils.shader.render(ShaderUtils.client.getTickDelta());
        }
    }

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    public void ait$getFov(CallbackInfoReturnable<Double> cir) {
        double d = cir.getReturnValueD();
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && MinecraftClient.getInstance().options.useKey.isPressed() && player.getMainHandStack().getItem() instanceof BaseGunItem) {
            cir.setReturnValue(d *= MathHelper.lerp(MinecraftClient.getInstance().options.getFovEffectScale().getValue(), 1.0d, 0.5d));
        }
    }
}
