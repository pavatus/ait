package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class OfficeDesktop extends TardisDesktopSchema {

    public OfficeDesktop() {
        super(new Identifier(AITMod.MOD_ID, "office"));
    }
}
