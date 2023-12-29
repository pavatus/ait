package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisDesktopSchema;

public class CaveDesktop extends TardisDesktopSchema {

    public CaveDesktop() {
        super(new Identifier(AITMod.MOD_ID, "cave"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "cave")),
                1024,
                1024
        ));
    }
}
