package mdteam.ait.core.desktops;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisDesktopSchema;

public class WarDesktop extends TardisDesktopSchema {

    public WarDesktop() {
        super(new Identifier(AITMod.MOD_ID, "war"));
    }
}
