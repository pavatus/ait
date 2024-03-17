package loqor.ait.client.renderers.wearables;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import loqor.ait.AITMod;
import loqor.ait.core.AITItems;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.util.AITModTags;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AITHudOverlay implements HudRenderCallback {

	@Override
	public void onHudRender(DrawContext drawContext, float v) {

		MinecraftClient mc = MinecraftClient.getInstance();

		// @TODO think about removing this - Loqor
        /*if(MinecraftClient.getInstance().player == null) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player.getEquippedStack(EquipmentSlot.HEAD).getItem() == AITItems.RESPIRATOR && mc.options.getPerspective().isFirstPerson()) {
            this.renderOverlay(drawContext, new Identifier(AITMod.MOD_ID, "textures/gui/overlay/maskblur.png"), 1.0F);
        }*/
		if(mc.player == null) return;
		if((mc.player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == AITItems.SONIC_SCREWDRIVER ||
				mc.player.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == AITItems.SONIC_SCREWDRIVER) && playerIsLookingAtSonicInteractable(mc.crosshairTarget, mc.player) && mc.options.getPerspective().isFirstPerson()) {
			this.renderOverlay(drawContext, new Identifier(AITMod.MOD_ID, "textures/gui/overlay/sonic_can_interact.png"), 1.0F);
		}
	}

	private boolean playerIsLookingAtSonicInteractable(HitResult crosshairTarget, PlayerEntity player) {
		if (player != null) {
			if (player.getMainHandStack().getItem() instanceof SonicItem) {
				ItemStack sonic = player.getMainHandStack();
				if(sonic == null) return false;
				NbtCompound nbt = sonic.getOrCreateNbt();
				if(!nbt.contains(SonicItem.FUEL_KEY)) return false;
				if (crosshairTarget.getType() == HitResult.Type.BLOCK) {
					Block block = player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos()).getBlock();
					return !(block instanceof AirBlock) && nbt.getDouble(SonicItem.FUEL_KEY) > 0 && player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos()).isIn(AITModTags.Blocks.SONIC_INTERACTABLE);
				}
			} else if (player.getOffHandStack().getItem() instanceof SonicItem) {
				ItemStack sonic = player.getOffHandStack();
				if(sonic == null) return false;
				NbtCompound nbt = sonic.getOrCreateNbt();
				if(!nbt.contains(SonicItem.FUEL_KEY)) return false;
				if (crosshairTarget.getType() == HitResult.Type.BLOCK) {
					Block block = player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos()).getBlock();
					return !(block instanceof AirBlock) && nbt.getDouble(SonicItem.FUEL_KEY) > 0 && player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos()).isIn(AITModTags.Blocks.SONIC_INTERACTABLE);
				}
			}
		}
		return false;
	}

	private void renderOverlay(DrawContext context, Identifier texture, float opacity) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		context.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		context.drawTexture(texture, (context.getScaledWindowWidth() / 2) - 8, (context.getScaledWindowHeight() / 2) - 8, 0, 0.0F, 0.0F, 16,  16, 16, 16);
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);
	}
}
