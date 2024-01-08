package mdteam.ait.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.FogShape;
import net.minecraft.util.math.MathHelper;

public class FoggyUtils {
    public static void overrideFog() {
        // i prefer fog to that hellish overlay anyday, but i still dont like it really todo find what we want
        if (ClientTardisUtil.isPlayerInATardis() && ClientTardisUtil.getAlarmDelta() != ClientTardisUtil.MAX_ALARM_DELTA_TICKS) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getAlarmDeltaForLerp(), -8, 10));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getAlarmDeltaForLerp(), 11, 32));
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0.5f,0,0,0.5f);
        }
        // spoooky black fog
        if (ClientTardisUtil.isPlayerInATardis() && ClientTardisUtil.getPowerDelta() != ClientTardisUtil.MAX_POWER_DELTA_TICKS) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), -8, 24));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), 11, 32));
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogColor(0,0,0, ClientTardisUtil.getCurrentTardis().isSiegeMode() ? 0.85f : 1f);
        }
    }
}
