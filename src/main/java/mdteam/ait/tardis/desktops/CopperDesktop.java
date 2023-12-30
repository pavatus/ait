package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class CopperDesktop extends TardisDesktopSchema {

    public CopperDesktop() {
        super(new Identifier(AITMod.MOD_ID, "copper"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "copper_temp")),
                400,
                400
        ));
    }
}
