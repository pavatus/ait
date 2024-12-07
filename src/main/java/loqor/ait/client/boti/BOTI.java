package loqor.ait.client.boti;

import static loqor.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer.FRAME_TEXTURE;
import static loqor.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer.PAINTING_TEXTURE;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.AITBufferBuilderStorage;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import loqor.ait.client.models.decoration.GallifreyFallsFrameModel;
import loqor.ait.client.models.decoration.GallifreyFallsModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.client.renderers.VortexUtil;


public class BOTI {

    public static BOTIHandler BOTI_HANDLER = new BOTIHandler();
    public static AITBufferBuilderStorage AIT_BUF_BUILDER_STORAGE = new AITBufferBuilderStorage();

    public static void renderGallifreyFallsPainting(MatrixStack stack, SinglePartEntityModel singlePartEntityModel, int light, VertexConsumerProvider provider) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        GallifreyFallsFrameModel model = new GallifreyFallsFrameModel(GallifreyFallsFrameModel.getTexturedModelData().createModel());

        model.render(stack, provider.getBuffer(RenderLayer.getEntityCutout(FRAME_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        stack.push();
        stack.translate(0, 0, -0.125);

        MinecraftClient.getInstance().getFramebuffer().endWrite();

        BOTI_HANDLER.setupFramebuffer();

        BOTI.copyFramebuffer(MinecraftClient.getInstance().getFramebuffer(), BOTI_HANDLER.afbo);

        VertexConsumerProvider.Immediate botiProvider = AIT_BUF_BUILDER_STORAGE.getBotiVertexConsumer();

        // Enable stencil testing and clear the stencil buffer
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilMask(0xFF);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        // Render the mask overtop the interior of the interior stuff

        RenderSystem.depthMask(true);
        stack.push();
        ((GallifreyFallsFrameModel) singlePartEntityModel).renderWithFbo(stack, botiProvider, null, light, OverlayTexture.DEFAULT_UV, 0, 0, 0, 1);
        botiProvider.draw();
        stack.pop();
        RenderSystem.depthMask(false);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        GlStateManager._depthFunc(GL11.GL_ALWAYS);

        // Render the falls model (this should only render inside the frame)
        GL11.glColorMask(true, true, true, false);
        stack.push();
        GallifreyFallsModel.getTexturedModelData().createModel().render(stack, botiProvider.getBuffer(AITRenderLayers.getBotiInterior(PAINTING_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        botiProvider.draw();
        stack.pop();
        GlStateManager._depthFunc(GL11.GL_LEQUAL);

        GL11.glColorMask(false, false, false, true);

        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        BOTI.copyFramebuffer(BOTI_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        GL11.glColorMask(true, true, true, true);

        stack.pop();
    }

    public static void renderInteriorDoorBoti(MatrixStack stack, Identifier frameTex, SinglePartEntityModel frame, ModelPart mask, Identifier interiorTex, SinglePartEntityModel interior, int light) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        // Render the FRAME of whatever you want to render here

        stack.push();
        stack.translate(0, 0.1, 0.8);
        stack.scale(0.635F, 0.631F, 0.631F);

        MinecraftClient.getInstance().getFramebuffer().endWrite();

        BOTI_HANDLER.setupFramebuffer();

        BOTI.copyFramebuffer(MinecraftClient.getInstance().getFramebuffer(), BOTI_HANDLER.afbo);

        VertexConsumerProvider.Immediate botiProvider = AIT_BUF_BUILDER_STORAGE.getBotiVertexConsumer();

        // Enable stencil testing and clear the stencil buffer
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilMask(0xFF);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        RenderSystem.depthMask(true);
        stack.push();
        mask.render(stack, botiProvider.getBuffer(RenderLayer.getEntityTranslucentCull(frameTex)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        botiProvider.draw();
        stack.pop();
        RenderSystem.depthMask(false);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        GlStateManager._depthFunc(GL11.GL_ALWAYS);

        GL11.glColorMask(true, true, true, false);
        stack.push();
        stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) Math.sin(MinecraftClient.getInstance().player.age / 100.0f * 600f)));
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) Math.sin(MinecraftClient.getInstance().player.age / 100.0f * 600f)));
        stack.translate(Math.sin(MinecraftClient.getInstance().player.age / 5.0f * 360f), -2.125f - Math.sin(MinecraftClient.getInstance().player.age / 20.0f * 360f), 400 + Math.cos(MinecraftClient.getInstance().player.age / 10.0f * 360f));
        VortexUtil util = new VortexUtil("space");
        util.renderVortex(stack);
        botiProvider.draw();
        stack.pop();
        GlStateManager._depthFunc(GL11.GL_LEQUAL);

        GL11.glColorMask(false, false, false, true);

        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        BOTI.copyFramebuffer(BOTI_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        GL11.glColorMask(true, true, true, true);

        stack.pop();
    }

    private static void copyFramebuffer(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_DEPTH_BUFFER_BIT | GlConst.GL_COLOR_BUFFER_BIT, GlConst.GL_NEAREST);
    }
}
