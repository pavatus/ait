/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package loqor.ait.client.renderers.entities;

import static com.mojang.blaze3d.platform.GlConst.GL_ALWAYS;
import static net.minecraft.client.MinecraftClient.IS_SYSTEM_MAC;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import loqor.ait.AITMod;
import loqor.ait.client.models.decoration.GallifreyFallsFrameModel;
import loqor.ait.client.models.decoration.GallifreyFallsModel;
import loqor.ait.core.entities.GallifreyFallsPaintingEntity;

@Environment(value=EnvType.CLIENT)
public class GallifreyFallsPaintingEntityRenderer
        extends EntityRenderer<GallifreyFallsPaintingEntity> {
    private Framebuffer framebuffer;
    public static final Identifier PAINTING_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/painting/gallifrey_falls.png");
    public static final Identifier FRAME_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/painting/frame.png");
    GallifreyFallsFrameModel frame;
    GallifreyFallsModel painting;
    public GallifreyFallsPaintingEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        frame = new GallifreyFallsFrameModel(GallifreyFallsFrameModel.getTexturedModelData().createModel());
        painting = new GallifreyFallsModel(GallifreyFallsModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(GallifreyFallsPaintingEntity paintingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180));
        matrixStack.translate(0, -0.5, 0.5);
        //frame.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(FRAME_TEXTURE)), 0xf00f0, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        Framebuffer tempBuffer = this.render(matrixStack, vertexConsumerProvider, MinecraftClient.getInstance().getFramebuffer());
        //tempBuffer.draw(MinecraftClient.getInstance().getFramebuffer().viewportWidth, MinecraftClient.getInstance().getFramebuffer().viewportHeight, true);

        frame.renderWithFbo(matrixStack, vertexConsumerProvider, MinecraftClient.getInstance().getFramebuffer(), i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        // painting.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntitySmoothCutout(PAINTING_TEXTURE)), 0xf00f0, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(paintingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Framebuffer render(MatrixStack stack, VertexConsumerProvider provider, Framebuffer mcFramebuffer) {
        if(this.framebuffer == null)
            this.framebuffer = new SimpleFramebuffer(mcFramebuffer.viewportWidth, mcFramebuffer.viewportHeight, true, IS_SYSTEM_MAC);

        //RenderSystem.enableBlend();
        //RenderSystem.defaultBlendFunc();

        // Sample rendering into the framebuffer
        //this.framebuffer.clear(IS_SYSTEM_MAC);
        //this.framebuffer.setClearColor(1, 0, 0, 0);
        //this.framebuffer.copyDepthFrom(mcFramebuffer);

        //this.framebuffer.beginWrite(false);
        // Add rendering code here
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, mcFramebuffer.getColorAttachment());
        RenderSystem.stencilFunc(GL_ALWAYS, 1, 0xFF);
        RenderSystem.stencilMask(0xFF);
        painting.render(stack, provider.getBuffer(RenderLayer.getEntityTranslucent(PAINTING_TEXTURE)), 0xf00f0, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        //this.framebuffer.endWrite();
        // render the framebuffer
        //this.framebuffer.draw(mcFramebuffer.textureWidth, mcFramebuffer.textureHeight, false);

        // restore the original framebuffer
        mcFramebuffer.beginWrite(false);



        return this.framebuffer;
    }

    @Override
    public Identifier getTexture(GallifreyFallsPaintingEntity entity) {
        return null;
    }

}
