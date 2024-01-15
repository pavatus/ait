package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class PlinthExterior extends ExteriorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/plinth");
    public PlinthExterior() {
        super(REFERENCE, "plinth");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
