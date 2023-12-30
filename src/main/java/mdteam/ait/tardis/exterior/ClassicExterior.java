package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class ClassicExterior extends ExteriorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/classic");
    public ClassicExterior() {
        super(REFERENCE, "classic");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
