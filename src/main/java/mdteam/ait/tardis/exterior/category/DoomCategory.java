package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class DoomCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/doom");
    public DoomCategory() {
        super(REFERENCE, "doom");
    }
}
