package loqor.ait.data.schema.desktop;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.desktop.textures.DesktopPreviewTexture;

public class DevDesktop extends TardisDesktopSchema {

    public DevDesktop() {
        super(new Identifier(AITMod.MOD_ID, "dev"),
                new DesktopPreviewTexture(DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "dev")),
                        800, 800),
                new Loyalty(Loyalty.Type.PILOT));
    }

    @Override
    public boolean freebie() {
        return false;
    }
}
