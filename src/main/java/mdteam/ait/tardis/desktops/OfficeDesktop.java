package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class OfficeDesktop extends TardisDesktopSchema {

    public OfficeDesktop() {
        super(new Identifier(AITMod.MOD_ID, "office"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "office")),
                1024,
                1024
        ));
    }
}
