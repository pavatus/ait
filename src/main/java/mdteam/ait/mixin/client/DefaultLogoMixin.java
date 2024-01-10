package mdteam.ait.mixin.client;

import mdteam.ait.AITMod;
import mdteam.ait.core.util.AITConfig;
import mdteam.ait.core.util.AITConfigModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LogoDrawer.class, priority = 1001)
public class DefaultLogoMixin {

    //having these two initialized like so doesn't entirely matter, it just makes stuff cleaner.
    private static final Identifier AIT_LOGO = new Identifier(AITMod.MOD_ID, "textures/gui/title/aitlogo.png");
    private static final Identifier AIT_EDITION = new Identifier(AITMod.MOD_ID, "textures/gui/title/edition.png");

    //My reasoning for this is because the "width" argument in the methods below doesn't actually get the active window's screen width, so I have to do it this way :/.
    private final MinecraftClient client = MinecraftClient.getInstance();

    //This is setting the logo to BOTH the logo itself and the edition, as they're both in the same texture.
    //Also, while on the topic, I had to make a new texture since the old one only worked for older versions of Minecraft, as 1.19+ now uses a scaled and blurred version of the logo for the title screen.
    @Redirect(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V", ordinal = 0))
    private void injectedLogo(DrawContext instance, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if (!AITMod.AIT_CONFIG.CUSTOM_MENU()) {
            instance.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
            return;
        }
        if (this.client.currentScreen == null) return;
        int screenWidth = this.client.currentScreen.width;
        int i = screenWidth / 2 - 128;
        instance.drawTexture(AIT_LOGO, i, y, 0.0f, 0.0f, 256, 64, 256, 64);
    }

    //This is setting the edition to be invisible since it's not necessary, as it's within the logo file.
    @Redirect(method = "Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/gui/DrawContext;IFI)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V", ordinal = 1))
    private void injectedEdition(DrawContext instance, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if (!AITMod.AIT_CONFIG.CUSTOM_MENU()) {
            instance.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
            return;
        }

        int screenWidth = this.client.currentScreen.width;
        int i = screenWidth / 2 - 128;
        instance.drawTexture(AIT_EDITION, i, y, 0.0f, 0.0f, 256, 44, 256, 64);
    }

}
