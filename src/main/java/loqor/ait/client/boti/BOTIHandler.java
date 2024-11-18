package loqor.ait.client.boti;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.util.Window;

public class BOTIHandler {
    public Framebuffer afbo;

    public void setupFramebuffer() {
        Window window = MinecraftClient.getInstance().getWindow();

        if (afbo != null && (afbo.viewportWidth != window.getFramebufferWidth() || afbo.viewportHeight != window.getFramebufferHeight())) {
            afbo = null;
        }

        if (afbo == null) {
            afbo = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
        }

        afbo.beginWrite(false);
        afbo.checkFramebufferStatus();

        if (!AITRenderHelper.getIsStencilEnabled(afbo)) {
            AITRenderHelper.setIsStencilEnabled(afbo, true);
        }
    }

    public void endFBO() {
        afbo.endWrite();
    }

}
