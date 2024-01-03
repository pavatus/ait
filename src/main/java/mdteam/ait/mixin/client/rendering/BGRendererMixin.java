package mdteam.ait.mixin.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.client.util.ShaderUtils;
import mdteam.ait.tardis.Tardis;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// tbh i don even know what this class is im just mixing into whatever i can at this point
@Mixin(BackgroundRenderer.class)
public class BGRendererMixin {
    @Inject(at = @At("TAIL"), method = "applyFog")
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        // todo move to a fogutils class because restarting the game everytime is annoying
        // i would do it but i no no wanna :(

        // i prefer fog to that hellish overlay anyday, but i still dont like it really todo find what we want
        if (ShaderUtils.enabled) {
            RenderSystem.setShaderFogStart(10);
            RenderSystem.setShaderFogEnd(32);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0.5f,0,0);
        }
        // spoooky black fog
        if (ClientTardisUtil.isPlayerInATardis() && ClientTardisUtil.getPowerDelta() != ClientTardisUtil.MAX_POWER_DELTA_TICKS) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), -8, 24));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), 11, 32));
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0,0,0);
        }
    }
}
