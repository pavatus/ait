package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.CopperConsole;
import mdteam.ait.tardis.console.CoralConsole;
import net.minecraft.util.Identifier;

public class CopperVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/copper");

    public CopperVariant() {
        super(CopperConsole.REFERENCE, REFERENCE);
    }
}
