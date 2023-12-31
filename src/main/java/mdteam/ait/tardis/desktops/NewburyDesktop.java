package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class NewburyDesktop extends TardisDesktopSchema {

    public NewburyDesktop() {
        super(new Identifier(AITMod.MOD_ID, "newbury"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "newbury")),
                1024,
                1024
        ));
    }
}
