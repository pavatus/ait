package loqor.ait.client.boti;

import static loqor.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer.PAINTING_TEXTURE;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.AITBufferBuilderStorage;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import loqor.ait.client.models.decoration.GallifreyFallsFrameModel;
import loqor.ait.client.models.decoration.GallifreyFallsModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.entities.GallifreyFallsPaintingEntity;

public class BOTI {

    public static BOTIHandler BOTI_HANDLER = new BOTIHandler();
    public static AITBufferBuilderStorage AIT_BUF_BUILDER_STORAGE = new AITBufferBuilderStorage();

    public static void renderBOTI(MatrixStack stack, SinglePartEntityModel singlePartEntityModel, int light, VertexConsumerProvider provider) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;



        stack.push();
        MinecraftClient.getInstance().getFramebuffer().endWrite();

        BOTI_HANDLER.setupFramebuffer();

        Vec3d skyColor = MinecraftClient.getInstance().world.getSkyColor(MinecraftClient.getInstance().player.getPos(),
                MinecraftClient.getInstance().world.getTime());

        VertexConsumerProvider.Immediate botiProvider = AIT_BUF_BUILDER_STORAGE.getBotiVertexConsumer();

        // Enable stencil testing and clear the stencil buffer

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilMask(0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

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
        GallifreyFallsModel.getTexturedModelData().createModel().render(stack, botiProvider.getBuffer(AITRenderLayers.getEntityTranslucent(PAINTING_TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_STENCIL_TEST);

// Draw the framebuffer
        BOTI_HANDLER.endFBO();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BOTI_HANDLER.afbo.draw(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight(), false);

// Clean up
        BOTI_HANDLER.afbo.clear(MinecraftClient.IS_SYSTEM_MAC);
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        stack.pop();
    }

    private static void renderWorld(MatrixStack stack, int light, VertexConsumerProvider consumer) {
        stack.push();

        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        BlockPos offset = new BlockPos(0, 0, 0);

        stack.scale(-1, -1, 1);

        stack.translate(offset.getX(), offset.getY(), offset.getZ());

        for (Entity e : MinecraftClient.getInstance().world.getEntities()) {
            if (e instanceof GallifreyFallsPaintingEntity) continue;
            stack.push();
            Vec3d pos = e.getPos().subtract(offset.toCenterPos());
            stack.translate(pos.getX(), pos.getY() - 8, pos.getZ());
            EntityRenderer<? super Entity> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(e);
            renderer.render(e, e.getBodyYaw(), e.getPitch(), stack, consumer, OverlayTexture.DEFAULT_UV);
            stack.pop();
        }

        stack.pop();
    }

    public static void stencilStart(MatrixStack stack, int light, ModelPart part, VertexConsumer consumer, VertexConsumerProvider.Immediate bufferProvider) {

    }

    public static void stencilEnd(MatrixStack stack, int light, ModelPart part, VertexConsumer consumer, VertexConsumerProvider.Immediate bufferProvider) {

    }
}
