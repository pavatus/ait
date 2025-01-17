package loqor.ait.data.schema.desktop;


import loqor.ait.AITMod;
import loqor.ait.data.schema.desktop.textures.DesktopPreviewTexture;

public class DefaultCaveDesktop extends TardisDesktopSchema {

    public DefaultCaveDesktop() {
        super(AITMod.id("default_cave"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(AITMod.id("cave"))));
    }

    @Override
    public boolean freebie() {
        return false;
    }
}
