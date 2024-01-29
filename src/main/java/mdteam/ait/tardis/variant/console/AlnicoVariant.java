package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.AlnicoConsole;
import mdteam.ait.tardis.console.CopperConsole;
import net.minecraft.util.Identifier;

public class AlnicoVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/alnico");

    public AlnicoVariant() {
        super(AlnicoConsole.REFERENCE, REFERENCE);
    }
}
