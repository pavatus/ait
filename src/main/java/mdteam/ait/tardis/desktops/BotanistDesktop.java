package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class BotanistDesktop extends TardisDesktopSchema {

    public BotanistDesktop() {
        super(new Identifier(AITMod.MOD_ID, "botanist"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "botanist")),
                1024,
                1024
        ));
    }
}
