package loqor.ait.client.boti;

import static loqor.ait.client.shaders.AITShaderRegistry.BLIT_SCREEN_NO_BLEND;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import loqor.ait.AITMod;

public class AITRenderHelper {

    public static void checkGlError() {
        int errorCode = GL11.glGetError();
        if (errorCode != GL_NO_ERROR) {
            AITMod.LOGGER.warn("OpenGL Error" + errorCode);
            new Throwable().printStackTrace();
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean getIsStencilEnabled(Framebuffer renderTarget) {
        return ((StencilFrameBuffer) renderTarget).ait$getIsStencilBufferEnabled();
    }

    @Environment(EnvType.CLIENT)
    public static void setIsStencilEnabled(Framebuffer renderTarget, boolean cond) {
        ((StencilFrameBuffer) renderTarget).ait$setIsStencilBufferEnabledAndReload(cond);
    }

    public static void drawFramebufferButSpecial(
            Framebuffer textureProvider, boolean doUseAlphaBlend, boolean doEnableModifyAlpha,
            int x, int y, int viewportWidth, int viewportHeight
    ) {
        checkGlError();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.viewport(x, textureProvider.viewportHeight - viewportHeight - y, viewportWidth, viewportHeight);

        if (doUseAlphaBlend) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.ONE,
                    GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SrcFactor.ZERO,
                    GlStateManager.DstFactor.ONE
            );
        }
        else {
            RenderSystem.disableBlend();
        }

        if (doEnableModifyAlpha) {
            RenderSystem.colorMask(true, true, true, true);
        }
        else {
            RenderSystem.colorMask(true, true, true, false);
        }

        ShaderProgram shader = doUseAlphaBlend ?
                MinecraftClient.getInstance().gameRenderer.blitScreenProgram : BLIT_SCREEN_NO_BLEND;

        Validate.notNull(shader, "shader is null");

        shader.addSampler("DiffuseSampler", textureProvider.getColorAttachment());
        shader.bind();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.BLIT_SCREEN);
        bufferBuilder.vertex(0.0f, 0.0f, 0.0f);
        bufferBuilder.vertex(1.0f, 0.0f, 0.0f);
        bufferBuilder.vertex(1.0f, 1.0f, 0.0f);
        bufferBuilder.vertex(0.0f, 1.0f, 0.0f);
        tessellator.draw();
        shader.close();

        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        checkGlError();
    }

}
