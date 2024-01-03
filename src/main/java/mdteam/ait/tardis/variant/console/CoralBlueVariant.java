package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.CoralConsole;
import mdteam.ait.tardis.console.HartnellConsole;
import net.minecraft.util.Identifier;

public class CoralBlueVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/coral_blue");

    public CoralBlueVariant() {
        super(CoralConsole.REFERENCE, REFERENCE);
    }
}
