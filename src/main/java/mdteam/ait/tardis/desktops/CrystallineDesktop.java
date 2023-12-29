package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class CrystallineDesktop extends TardisDesktopSchema {

    public CrystallineDesktop() {
        super(new Identifier(AITMod.MOD_ID, "crystalline"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "crystalline")),
                1024,
                1024
        ));
    }
}
