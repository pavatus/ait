package mdteam.ait.core.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class PristineDesktop extends TardisDesktopSchema {

    public PristineDesktop() {
        super(new Identifier(AITMod.MOD_ID, "pristine"));
    }
}
