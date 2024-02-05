package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class ClassicCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/classic");
    public ClassicCategory() {
        super(REFERENCE, "classic");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
