package mdteam.ait.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.FogShape;
import net.minecraft.util.math.MathHelper;

public class FoggyUtils {
    public static void overrideFog() {
        if (ClientTardisUtil.isPlayerInATardis() && ClientTardisUtil.getAlarmDelta() != ClientTardisUtil.MAX_ALARM_DELTA_TICKS) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getAlarmDeltaForLerp(), -8, 10));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getAlarmDeltaForLerp(), 11, 32));
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0.5f,0,0,0.5f);
            MinecraftClient.getInstance().gameRenderer.getCamera().getSubmersionType();
        }
        if (ClientTardisUtil.isPlayerInATardis() && !ClientTardisUtil.getCurrentTardis().isGrowth() && ClientTardisUtil.getPowerDelta() != ClientTardisUtil.MAX_POWER_DELTA_TICKS) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), -8, 24));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), 11, 32));
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0,0,0, ClientTardisUtil.getCurrentTardis().isSiegeMode() ? 0.85f : 1f);
        }
        // TODO for some reason the fog doesnt turn off once it turns on idk its weird
        if (ClientTardisUtil.isPlayerInATardis() && ClientTardisUtil.getCurrentTardis().getHandlers().getCrashData().isToxic()) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, -8, 24));;
            RenderSystem.setShaderFogEnd(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, 11, 32));
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0f,0.8f,0f, 0.35f);
        }
    }
}
