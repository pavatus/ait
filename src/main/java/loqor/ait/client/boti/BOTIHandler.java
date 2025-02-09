package loqor.ait.client.boti;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.util.Window;

public class BOTIHandler {
    public Framebuffer afbo;
    public SecondaryFrameBuffer sfbo = new SecondaryFrameBuffer();

    public void beforeRendering() {
        sfbo.prepare();

        sfbo.fb.setClearColor(1, 0, 0, 0);
        sfbo.fb.clear(MinecraftClient.IS_SYSTEM_MAC);

        AITIrisHelper.newCopyDepthStencil(MinecraftClient.getInstance().getFramebuffer(), sfbo.fb);
        AITIrisHelper.copyColor(MinecraftClient.getInstance().getFramebuffer(), sfbo.fb);

        AITRenderHelper.setIsStencilEnabled(MinecraftClient.getInstance().getFramebuffer(), false);

        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
    }

    public void useDeferredRendering() {
        sfbo.fb.beginWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SrcFactor.ZERO,
                GlStateManager.DstFactor.ONE
        );
        AITRenderHelper.drawFramebufferButSpecial(sfbo.fb, true, true,
                0, 0,
                MinecraftClient.getInstance().getFramebuffer().textureWidth,
                MinecraftClient.getInstance().getFramebuffer().textureHeight);
    }

    public void setupFramebuffer() {

        Window window = MinecraftClient.getInstance().getWindow();

        if (afbo == null || afbo.textureWidth != window.getFramebufferWidth() || afbo.textureHeight != window.getFramebufferHeight()) {
            afbo = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);;
        }

        afbo.beginWrite(false);
        afbo.checkFramebufferStatus();

        if (!AITRenderHelper.getIsStencilEnabled(afbo)) {
            AITRenderHelper.setIsStencilEnabled(afbo, true);
        }
    }

    public void endFBO() {
        afbo.clear(MinecraftClient.IS_SYSTEM_MAC);
        afbo.endWrite();
    }

}
