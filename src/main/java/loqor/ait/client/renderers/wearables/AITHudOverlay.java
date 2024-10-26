package loqor.ait.client.renderers.wearables;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import loqor.ait.AITMod;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITTags;
import loqor.ait.core.config.AITConfig;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.item.SpacesuitItem;
import loqor.ait.core.planet.Planet;
import loqor.ait.core.planet.PlanetRegistry;

public class AITHudOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float v) {

        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer textRenderer = mc.textRenderer;

        Planet planet = PlanetRegistry.getInstance().get(mc.player.getWorld());

        if(mc.player == null || planet == null) return;
        if(mc.player.getEquippedStack(EquipmentSlot.HEAD).getItem() == AITItems.SPACESUIT_HELMET && mc.options.getPerspective().isFirstPerson()) {
            MatrixStack stack = drawContext.getMatrices();
            stack.push();
            stack.scale(1.5f, 1.5f, 1.5f);
            drawContext.drawTextWithShadow(textRenderer,
                    Text.literal(this.getTemperatureType(AITMod.AIT_CONFIG, planet)),
                    0, 0, 0xFFFFFF);
            stack.pop();
            stack.push();
            stack.scale(1.5f, 1.5f, 1.5f);
            String oxygen = "" + Planet.getOxygenInTank(mc.player);
            drawContext.drawTextWithShadow(textRenderer, Text.literal(
                    oxygen.substring(0, 3) + "L / " + SpacesuitItem.MAX_OXYGEN + "L"), 0, 50, 0xFFFFFF);
            stack.pop();
        }

        if (mc.player == null)
            return;
        if ((mc.player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == AITItems.SONIC_SCREWDRIVER
                || mc.player.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == AITItems.SONIC_SCREWDRIVER)
                && playerIsLookingAtSonicInteractable(mc.crosshairTarget, mc.player)
                && mc.options.getPerspective().isFirstPerson()) {
            this.renderOverlay(drawContext,
                    new Identifier(AITMod.MOD_ID, "textures/gui/overlay/sonic_can_interact.png"), 1.0F);
        }
    }

    private boolean playerIsLookingAtSonicInteractable(HitResult crosshairTarget, PlayerEntity player) {
        if (player != null) {
            if (player.getMainHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getMainHandStack();
                if (sonic == null)
                    return false;
                NbtCompound nbt = sonic.getOrCreateNbt();
                if (!nbt.contains(SonicItem.FUEL_KEY))
                    return false;
                if (crosshairTarget.getType() == HitResult.Type.BLOCK) {
                    Block block = player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                            .getBlock();
                    return !(block instanceof AirBlock) && nbt.getDouble(SonicItem.FUEL_KEY) > 0
                            && player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                                    .isIn(AITTags.Blocks.SONIC_INTERACTABLE);
                }
            } else if (player.getOffHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getOffHandStack();
                if (sonic == null)
                    return false;
                NbtCompound nbt = sonic.getOrCreateNbt();
                if (!nbt.contains(SonicItem.FUEL_KEY))
                    return false;
                if (crosshairTarget.getType() == HitResult.Type.BLOCK) {
                    Block block = player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                            .getBlock();
                    return !(block instanceof AirBlock) && nbt.getDouble(SonicItem.FUEL_KEY) > 0
                            && player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                                    .isIn(AITTags.Blocks.SONIC_INTERACTABLE);
                }
            }
        }
        return false;
    }

    private void renderOverlay(DrawContext context, Identifier texture, float opacity) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        context.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ZERO);
        context.drawTexture(texture, (context.getScaledWindowWidth() / 2) - 8,
                (context.getScaledWindowHeight() / 2) - 8, 0, 0.0F, 0.0F, 16, 16, 16, 16);
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);
    }

    public String getTemperatureType(AITConfig config, Planet planet) {
        String c = "" + planet.celcius();
        String f = "" + planet.fahrenheit();
        return switch(config.TEMPERATURE_TYPE()) {
            default -> c.substring(0, 5) + "°C";
            case FAHRENHEIT -> f.substring(0, 5) + "°F";
            case KELVIN -> planet.kelvin() + "K";
        };
    }
}
