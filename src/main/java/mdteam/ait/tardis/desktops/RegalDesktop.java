package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class RegalDesktop extends TardisDesktopSchema {

    public RegalDesktop() {
        super(new Identifier(AITMod.MOD_ID, "regal"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "regal")),
                1024,
                1024
        ));
    }
}
