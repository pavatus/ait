package mdteam.ait.core.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class BotanistDesktop extends TardisDesktopSchema {

    public BotanistDesktop() {
        super(new Identifier(AITMod.MOD_ID, "botanist"));
    }
}
