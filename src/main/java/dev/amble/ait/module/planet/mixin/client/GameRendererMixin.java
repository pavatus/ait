package dev.amble.ait.module.planet.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

import dev.amble.ait.client.tardis.ClientTardis;
import dev.amble.ait.client.util.ClientTardisUtil;
import dev.amble.ait.core.AITDimensions;
import dev.amble.ait.core.world.TardisServerWorld;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFarPlaneDistance", at = @At("HEAD"), cancellable = true)
    private void ait$getFarPlaneDistance(CallbackInfoReturnable<Float> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return;
        boolean ifTardisWorld = TardisServerWorld.isTardisDimension(mc.world);
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();
        if (mc.world.getRegistryKey().equals(AITDimensions.SPACE) || ifTardisWorld) {
            cir.setReturnValue(64.0F * 64.0F);
        }
    }
}
