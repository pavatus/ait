package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class ToyotaDesktop extends TardisDesktopSchema {

    public ToyotaDesktop() {
        super(new Identifier(AITMod.MOD_ID, "toyota"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "toyota")),
                1024,
                1024
        ));
    }
}
