package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class Type40Desktop extends TardisDesktopSchema {

    public Type40Desktop() {
        super(new Identifier(AITMod.MOD_ID, "type_40"), new DesktopPreviewTexture(
                DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "type_40")),
                1024,
                1024
        ));
    }
}
