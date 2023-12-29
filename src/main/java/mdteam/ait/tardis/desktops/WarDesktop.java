package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisDesktopSchema;

public class WarDesktop extends TardisDesktopSchema {

    public WarDesktop() {
        super(new Identifier(AITMod.MOD_ID, "war"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "war")),
                1024,
                1024
        ));
    }
}
