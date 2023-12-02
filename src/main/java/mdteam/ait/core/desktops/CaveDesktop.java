package mdteam.ait.core.desktops;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisDesktopSchema;

public class CaveDesktop extends TardisDesktopSchema {

    public CaveDesktop() {
        super(new Identifier(AITMod.MOD_ID, "cave"));
    }
}
