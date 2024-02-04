package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class PoliceBoxCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/police_box");
    public PoliceBoxCategory() {
        super(REFERENCE, "police_box");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}

