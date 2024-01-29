package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class TardimExterior extends ExteriorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/tardim");
    public TardimExterior() {
        super(REFERENCE, "tardim");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
