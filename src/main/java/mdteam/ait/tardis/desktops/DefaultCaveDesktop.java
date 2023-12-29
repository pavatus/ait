package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class DefaultCaveDesktop extends TardisDesktopSchema {

    public DefaultCaveDesktop() {
        super(new Identifier(AITMod.MOD_ID, "default_cave"));
    }

    @Override
    public boolean freebie() {
        return false;
    }
}
