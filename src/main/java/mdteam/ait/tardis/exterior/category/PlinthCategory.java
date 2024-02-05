package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class PlinthCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/plinth");
    public PlinthCategory() {
        super(REFERENCE, "plinth");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
