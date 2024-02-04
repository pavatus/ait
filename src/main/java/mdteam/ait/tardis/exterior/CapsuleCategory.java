package mdteam.ait.tardis.exterior;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class CapsuleCategory extends ExteriorCategory {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/capsule");

    public CapsuleCategory() {
        super(REFERENCE, "capsule");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }
}
