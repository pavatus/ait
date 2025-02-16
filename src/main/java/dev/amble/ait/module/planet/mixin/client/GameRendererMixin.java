package dev.amble.ait.module.planet.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

import dev.amble.ait.core.AITDimensions;
import dev.amble.ait.core.world.TardisServerWorld;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFarPlaneDistance", at = @At("HEAD"), cancellable = true)
    private void ait$getFarPlaneDistance(CallbackInfoReturnable<Float> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return;
        if (mc.world.getRegistryKey().equals(AITDimensions.SPACE) || TardisServerWorld.isTardisDimension(mc.world))
            cir.setReturnValue((32.0F * 16.0F) * 64.0F);
    }
}
