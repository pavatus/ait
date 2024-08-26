package loqor.ait.data.schema.desktop;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.desktops.textures.DesktopPreviewTexture;

public class DefaultCaveDesktop extends TardisDesktopSchema {

    public DefaultCaveDesktop() {
        super(new Identifier(AITMod.MOD_ID, "default_cave"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "cave"))));
    }

    @Override
    public boolean freebie() {
        return false;
    }
}
