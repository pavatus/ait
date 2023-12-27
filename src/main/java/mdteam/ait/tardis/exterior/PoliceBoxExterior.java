package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class PoliceBoxExterior extends ExteriorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/police_box");
    public PoliceBoxExterior() {
        super(REFERENCE);
    }
}
