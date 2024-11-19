package loqor.ait.client.boti;

import static loqor.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer.PAINTING_TEXTURE;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
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

        stack.push();
        MinecraftClient.getInstance().getFramebuffer().endWrite();
        BOTI_HANDLER.setupFramebuffer();

        VertexConsumerProvider.Immediate botiProvider = AIT_BUF_BUILDER_STORAGE.getBotiVertexConsumer();

        // Enable stencil testing and clear the stencil buffer
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_INCR);
        GL11.glStencilMask(0xFF);

        // Render the frame model to the stencil buffer
        GL11.glColorMask(true, true, true, false);
        ((GallifreyFallsFrameModel) singlePartEntityModel).renderWithFbo(stack, botiProvider, null, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColorMask(false, false, false, true);

        // Set up stencil test to only render where the stencil buffer is equal to 1
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        GL11.glStencilMask(0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);

        // Render the falls model (this should only render inside the frame)
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        stack.push();
        GallifreyFallsModel.getTexturedModelData().createModel().render(stack, botiProvider.getBuffer(AITRenderLayers.getEntityCutoutNoCull(PAINTING_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.pop();
        GL11.glDisable(GL11.GL_STENCIL_TEST);

        // Draw the framebuffer

        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        BOTI_HANDLER.afbo.draw(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight(), false);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();

        // Clean up
        BOTI_HANDLER.endFBO();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        stack.pop();
    }
}
