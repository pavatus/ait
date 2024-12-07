package loqor.ait.client.boti;

import static loqor.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer.FRAME_TEXTURE;
import static loqor.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer.PAINTING_TEXTURE;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.AITBufferBuilderStorage;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

import loqor.ait.client.models.decoration.GallifreyFallsFrameModel;
import loqor.ait.client.models.decoration.GallifreyFallsModel;
import loqor.ait.client.renderers.AITRenderLayers;


public class BOTI {

    public static BOTIHandler BOTI_HANDLER = new BOTIHandler();
    public static AITBufferBuilderStorage AIT_BUF_BUILDER_STORAGE = new AITBufferBuilderStorage();

    public static void renderBOTI(MatrixStack stack, SinglePartEntityModel singlePartEntityModel, int light, VertexConsumerProvider provider) {
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
        VertexConsumerProvider.Immediate nonBotiProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        // Enable stencil testing and clear the stencil buffer
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilMask(0xFF);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        // Render the frame model to the stencil buffer
        RenderSystem.depthMask(false);
        stack.push();
        ((GallifreyFallsFrameModel) singlePartEntityModel).renderWithFbo(stack, botiProvider, null, light, OverlayTexture.DEFAULT_UV, 0.0F, 0.0F, 0.0F, 1.0F);
        botiProvider.draw();
        stack.pop();
        RenderSystem.depthMask(true);

        // Set up stencil test to only render where the stencil buffer is equal to 1
        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        GlStateManager._depthFunc(GL11.GL_ALWAYS);

        // Render the falls model (this should only render inside the frame)
        GL11.glColorMask(true, true, true, false);
        stack.push();
        GallifreyFallsModel.getTexturedModelData().createModel().render(stack, botiProvider.getBuffer(AITRenderLayers.getEntityCutoutNoCull(PAINTING_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        botiProvider.draw();
        stack.pop();
        GL11.glColorMask(false, false, false, true);

        GlStateManager._depthFunc(GL11.GL_LEQUAL);

        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        BOTI.copyFramebuffer(BOTI_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        // Clean up
        stack.pop();

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);

        GL11.glColorMask(true, true, true, true);

    }

    private static void copyFramebuffer(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_DEPTH_BUFFER_BIT | GlConst.GL_COLOR_BUFFER_BIT, GlConst.GL_NEAREST);
    }
}
