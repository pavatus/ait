package loqor.ait.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import loqor.ait.core.AITItems;
import loqor.ait.tardis.Tardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.MathHelper;

public class FoggyUtils {
	public static void overrideFog() {
		if (ClientTardisUtil.isPlayerInATardis() && ClientTardisUtil.getAlarmDelta() != ClientTardisUtil.MAX_ALARM_DELTA_TICKS) {
			RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getAlarmDeltaForLerp(), -8, 10));
			RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getAlarmDeltaForLerp(), 11, 32));
			RenderSystem.setShaderFogShape(FogShape.SPHERE);
			RenderSystem.setShaderFogColor(0.5f, 0, 0, 0.5f);
			MinecraftClient.getInstance().gameRenderer.getCamera().getSubmersionType();
		}
		Tardis tardis = ClientTardisUtil.getCurrentTardis();
		
		if (tardis == null)
			return;
		
		if (ClientTardisUtil.isPlayerInATardis() && !tardis.isGrowth() && ClientTardisUtil.getPowerDelta() != ClientTardisUtil.MAX_POWER_DELTA_TICKS) {
			RenderSystem.setShaderFogStart(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), -8, 24));
			RenderSystem.setShaderFogEnd(MathHelper.lerp(ClientTardisUtil.getPowerDeltaForLerp(), 11, 32));
			RenderSystem.setShaderFogShape(FogShape.SPHERE);
			RenderSystem.setShaderFogColor(0, 0, 0, tardis.siege().isActive() ? 0.85f : 1f);
		}
		if (ClientTardisUtil.isPlayerInATardis() && tardis.crash().isToxic() &&
				tardis.engine().hasPower()) {
			RenderSystem.setShaderFogStart(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, -8, 24));
			RenderSystem.setShaderFogEnd(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, 11, 32));
			RenderSystem.setShaderFogShape(FogShape.SPHERE);
			RenderSystem.setShaderFogColor(0.2f, 0.2f, 0.2f, MinecraftClient.getInstance().player
					.getEquippedStack(EquipmentSlot.HEAD).getItem() == AITItems.RESPIRATOR ? 0.015f: 0.35f);
		}
		if (ClientTardisUtil.isPlayerInATardis() &&
				!tardis.crash().isToxic() &&
				!tardis.alarm().isEnabled() &&
				tardis.engine().hasPower()) {
			RenderSystem.setShaderFogStart(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, -8, 24));
			RenderSystem.setShaderFogEnd(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, 11, 32));
			RenderSystem.setShaderFogShape(FogShape.CYLINDER);
			int loyaltyLevel = tardis.loyalty().get(MinecraftClient.getInstance().player).level();
			RenderSystem.setShaderFogColor(1 - loyaltyLevel / 100f, 0, loyaltyLevel / 100f, 0.025f);
		}
		/*if(MinecraftClient.getInstance().world != null &&MinecraftClient.getInstance().world.getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD) {
			RenderSystem.setShaderFogStart(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, -8, 24));
			RenderSystem.setShaderFogEnd(MathHelper.lerp(MinecraftClient.getInstance().getTickDelta() / 100f, 11, 32));
			RenderSystem.setShaderFogShape(FogShape.SPHERE);
			RenderSystem.setShaderFogColor(0.3f, 0.3f, 0.3f, 0.35f);
		}*/
	}
}
