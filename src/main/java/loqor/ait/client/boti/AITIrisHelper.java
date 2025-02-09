package loqor.ait.client.boti;

import static org.lwjgl.opengl.GL11.*;

import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.opengl.GL43C;

import net.minecraft.client.gl.Framebuffer;

public class AITIrisHelper {
    public static void copyDepthStencil(Framebuffer from, Framebuffer to,boolean copyDepth, boolean copyStencil) {
        from.endWrite();

        int mask = 0;

        if (copyDepth) {
            if (copyStencil) {
                mask = GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT;
            }
            else {
                mask = GL_DEPTH_BUFFER_BIT;
            }
        }
        else {
            if (copyStencil) {
                mask = GL_STENCIL_BUFFER_BIT;
            }
            else {
                throw new RuntimeException();
            }
        }

        GlStateManager._glBindFramebuffer(GL30C.GL_READ_FRAMEBUFFER, from.fbo);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, to.fbo);

        GL30.glBlitFramebuffer(0, 0, from.textureWidth, from.textureHeight,
                0, 0, to.textureWidth, to.textureHeight,mask, GL_NEAREST);

        from.endWrite();
    }

    private static boolean isCopyImageSubDataSupported() {
        return GL.getCapabilities().glCopyImageSubData != 0;
    }

    public static void newCopyDepthStencil(Framebuffer from, Framebuffer to) {
        GL43C.glCopyImageSubData(from.getDepthAttachment(),GL43C.GL_TEXTURE_2D,0,0,0,0,
                to.getDepthAttachment(),GL43C.GL_TEXTURE_2D,0,0,0,0,from.textureWidth,from.textureHeight,1
        );
    }

    public static void copyColor(Framebuffer from, Framebuffer to) {
        GL43C.glCopyImageSubData(from.getColorAttachment(),GL43C.GL_TEXTURE_2D,0,0,0,0,
                to.getColorAttachment(),GL43C.GL_TEXTURE_2D,0,0,0,0,from.textureWidth,from.textureHeight,1);
    }
}
