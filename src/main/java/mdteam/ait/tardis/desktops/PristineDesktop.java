package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class PristineDesktop extends TardisDesktopSchema {

    public PristineDesktop() {
        super(new Identifier(AITMod.MOD_ID, "pristine"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "pristine")),
                1024,
                1024
        ));
    }
}
