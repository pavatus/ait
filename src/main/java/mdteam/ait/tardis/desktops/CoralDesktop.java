package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class CoralDesktop extends TardisDesktopSchema {

    public CoralDesktop() {
        super(new Identifier(AITMod.MOD_ID, "coral"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "coral")),
                800,
                800
        ));
    }
}
