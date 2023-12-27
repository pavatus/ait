package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class CubeExterior extends ExteriorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/cube");
    public CubeExterior() {
        super(REFERENCE);
    }
}
