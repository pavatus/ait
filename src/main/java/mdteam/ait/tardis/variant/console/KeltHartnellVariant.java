package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.HartnellConsoleModel;
import mdteam.ait.tardis.console.HartnellConsole;
import net.minecraft.util.Identifier;

public class KeltHartnellVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell_kelt");

    public KeltHartnellVariant() {
        super(HartnellConsole.REFERENCE, REFERENCE);
    }
}
