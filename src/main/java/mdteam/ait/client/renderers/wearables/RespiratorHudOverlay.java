package mdteam.ait.client.renderers.wearables;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class RespiratorHudOverlay implements HudRenderCallback {

	@Override
	public void onHudRender(DrawContext drawContext, float v) {

		// @TODO think about removing this - Loqor
        /*if(MinecraftClient.getInstance().player == null) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player.getEquippedStack(EquipmentSlot.HEAD).getItem() == AITItems.RESPIRATOR && mc.options.getPerspective().isFirstPerson()) {
            this.renderOverlay(drawContext, new Identifier(AITMod.MOD_ID, "textures/gui/overlay/maskblur.png"), 1.0F);
        }*/
	}

	private void renderOverlay(DrawContext context, Identifier texture, float opacity) {
		//RenderSystem.disableDepthTest();
		//RenderSystem.depthMask(false);
		context.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		context.drawTexture(texture, 0, 0, -100, 0.0F, 0.0F, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
		//RenderSystem.depthMask(true);
		//RenderSystem.enableDepthTest();
		context.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);
	}
}
