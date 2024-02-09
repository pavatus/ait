package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class RenegadeCategory extends ExteriorCategorySchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/renegade");
    public RenegadeCategory() {
        super(REFERENCE, "renegade");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
