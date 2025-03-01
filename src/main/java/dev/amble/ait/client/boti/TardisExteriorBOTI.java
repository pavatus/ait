package dev.amble.ait.client.boti;

import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import dev.amble.ait.AITMod;
import dev.amble.ait.client.models.exteriors.ExteriorModel;
import dev.amble.ait.client.renderers.AITRenderLayers;
import dev.amble.ait.compat.DependencyChecker;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.handler.travel.TravelHandlerBase;
import dev.amble.ait.core.tardis.util.network.c2s.BOTIChunkRequestC2SPacket;
import dev.amble.ait.core.tardis.util.network.s2c.BOTIDataS2CPacket;
import dev.amble.ait.data.schema.exterior.ClientExteriorVariantSchema;

public class TardisExteriorBOTI extends BOTI {
    private float lastRenderTick = -1;
    public void renderExteriorBoti(ExteriorBlockEntity exterior, ClientExteriorVariantSchema variant, MatrixStack stack, Identifier frameTex, SinglePartEntityModel frame, ModelPart mask, int light) {
        if (!AITMod.CONFIG.CLIENT.ENABLE_TARDIS_BOTI)
            return;

        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        stack.push();

        MinecraftClient.getInstance().getFramebuffer().endWrite();

        BOTI_HANDLER.setupFramebuffer();

        Vec3d skyColor = MinecraftClient.getInstance().world.getSkyColor(MinecraftClient.getInstance().player.getPos(), MinecraftClient.getInstance().getTickDelta());
        BOTI.setFramebufferColor(BOTI_HANDLER.afbo, (float) skyColor.x, (float) skyColor.y, (float) skyColor.z, 0);

        BOTI.copyFramebuffer(MinecraftClient.getInstance().getFramebuffer(), BOTI_HANDLER.afbo);

        VertexConsumerProvider.Immediate botiProvider = AIT_BUF_BUILDER_STORAGE.getBotiVertexConsumer();

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilMask(0xFF);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        RenderSystem.depthMask(true);
        stack.push();
        Vec3d vec = variant.parent().adjustPortalPos(new Vec3d(0, -1.1725f, 0), (byte) 0);
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        stack.translate(vec.x, vec.y, vec.z);
        stack.scale((float) variant.parent().portalWidth(), (float) variant.parent().portalHeight(), 1f);

        if (exterior.tardis().get().travel().getState() == TravelHandlerBase.State.LANDED)
            mask.render(stack, botiProvider.getBuffer(RenderLayer.getEndGateway()), 0xf000f0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        else
            mask.render(stack, botiProvider.getBuffer(RenderLayer.getEntityTranslucentCull(frameTex)), 0xf000f0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        botiProvider.draw();
        stack.pop();

        copyDepth(BOTI_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        BOTI_HANDLER.afbo.beginWrite(false);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);

        long currentTime = MinecraftClient.getInstance().getRenderTime();
        if (MinecraftClient.getInstance().getTickDelta() == lastRenderTick) {
            stack.pop();
            return;
        }

        lastRenderTick = MinecraftClient.getInstance().getTickDelta();

        if (currentTime - exterior.lastRequestTime >= 20) {
            updateChunkModel(exterior);
            exterior.lastRequestTime = currentTime;
        }

        if (AITMod.CONFIG.CLIENT.SHOULD_RENDER_BOTI_INTERIOR) {
            stack.push();
            stack.scale(1f, -1f, 1f);
            if (exterior.tardis().get().stats().targetPos() != null && exterior.tardis().get().stats().getTargetWorld() != null) {
                if (exterior.tardis().get().stats().chunkModel != null) {
                    BakedModel chunkMesh = exterior.tardis().get().stats().chunkModel;
                    VertexConsumer chunkConsumer = botiProvider.getBuffer(RenderLayer.getCutout());
                    MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
                            .render(stack.peek(),
                                    chunkConsumer,
                                    null,
                                    chunkMesh, 1, 1, 1, light,
                                    OverlayTexture.DEFAULT_UV);
                }

                for (Map.Entry<BlockPos, BlockEntity> entry : exterior.tardis().get().stats().blockEntities.entrySet()) {
                    BlockPos offsetPos = entry.getKey();
                    BlockEntity be = entry.getValue();
                    BlockEntityRenderer<BlockEntity> renderer = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(be);
                    if (renderer != null) {
                        stack.push();
                        stack.translate(offsetPos.getX() - 8, offsetPos.getY() - 8, offsetPos.getZ() - 8);
                        renderer.render(be, MinecraftClient.getInstance().getTickDelta(), stack, botiProvider, light, OverlayTexture.DEFAULT_UV);
                        stack.pop();                    } else {
                        System.out.println("No renderer found for block entity " + be + " at " + offsetPos);
                    }
                }
            }
            botiProvider.drawCurrentLayer();
            stack.pop();
        }

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        ((ExteriorModel) frame).renderDoors(exterior, frame.getPart(), stack, botiProvider.getBuffer(AITRenderLayers.getBotiInterior(variant.texture())), light, OverlayTexture.DEFAULT_UV, 1, 1F, 1.0F, 1.0F, true);
        botiProvider.draw();
        stack.pop();

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        if (variant.emission() != null)
            ((ExteriorModel) frame).renderDoors(exterior, frame.getPart(), stack, botiProvider.getBuffer(DependencyChecker.hasIris() ? AITRenderLayers.tardisEmissiveCullZOffset(variant.emission(), true) : AITRenderLayers.getBeaconBeam(variant.emission(), true)), 0xf000f0,
                    OverlayTexture.DEFAULT_UV, exterior.tardis().get().alarm().enabled().get() ?
                            !exterior.tardis().get().fuel().hasPower() ? 0.25f : 1f : 1f,
                    exterior.tardis().get().alarm().enabled().get() ? !exterior.tardis().get().fuel().hasPower() ? 0.01f : 0.3f : 1f,
                    exterior.tardis().get().alarm().enabled().get() ? !exterior.tardis().get().fuel().hasPower() ? 0.01f : 0.3f : 1f, 1f, true);
        botiProvider.draw();
        stack.pop();

        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        BOTI.copyColor(BOTI_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        stack.pop();
    }

    private void updateChunkModel(ExteriorBlockEntity exteriorBlockEntity) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world == null || exteriorBlockEntity.getWorld() == null || exteriorBlockEntity.tardis() == null) return;

        Tardis tardis = exteriorBlockEntity.tardis().get();

        long currentTime = mc.world.getTime();
        if (exteriorBlockEntity.lastRequestTime == 0 || currentTime - exteriorBlockEntity.lastRequestTime >= 20) {
            World targetWorld = mc.world.getRegistryKey() == tardis.stats().getTargetWorld() ? mc.world : null;
            if (targetWorld != null) {
                ChunkPos chunkPos = new ChunkPos(tardis.stats().targetPos());
                WorldChunk chunk = targetWorld.getChunk(chunkPos.x, chunkPos.z);
                tardis.stats().updateChunkModel(exteriorBlockEntity, new BOTIDataS2CPacket(exteriorBlockEntity.getPos(), chunk, tardis.stats().targetPos()).chunkData);
            } else {
                ClientPlayNetworking.send(new BOTIChunkRequestC2SPacket(exteriorBlockEntity.getPos(), tardis.stats().getTargetWorld(), tardis.stats().targetPos()));
            }
            exteriorBlockEntity.lastRequestTime = currentTime;
        }
    }
}
