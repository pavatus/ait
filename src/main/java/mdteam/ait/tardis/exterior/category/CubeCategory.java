package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class CubeCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/cube");
    public CubeCategory() {
        super(REFERENCE, "cube");
    }
}
