package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class TardimCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/tardim");
    public TardimCategory() {
        super(REFERENCE, "tardim");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
