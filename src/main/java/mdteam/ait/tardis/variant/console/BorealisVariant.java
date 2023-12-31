package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.BorealisConsoleModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.tardis.console.BorealisConsole;
import net.minecraft.util.Identifier;

public class BorealisVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/borealis");

    public BorealisVariant() {
        super(BorealisConsole.REFERENCE, REFERENCE);
    }
}
