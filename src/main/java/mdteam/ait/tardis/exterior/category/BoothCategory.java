package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class BoothCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/booth");
    public BoothCategory() {
        super(REFERENCE, "booth");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
