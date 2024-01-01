package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class GrowthExterior extends ExteriorSchema {

    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/coral_growth");
    public GrowthExterior() {
        super(REFERENCE, "coral_growth");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
