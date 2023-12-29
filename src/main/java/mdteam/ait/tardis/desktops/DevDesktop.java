package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class DevDesktop extends TardisDesktopSchema {

    public DevDesktop() {
        super(new Identifier(AITMod.MOD_ID, "dev"));
    }

    @Override
    public boolean freebie() {
        return false;
    }
}
