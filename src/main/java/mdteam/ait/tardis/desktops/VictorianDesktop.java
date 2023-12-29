package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class VictorianDesktop extends TardisDesktopSchema {

    public VictorianDesktop() {
        super(new Identifier(AITMod.MOD_ID, "victorian"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "victorian")),
                1024,
                1024
        ));
    }
}
