package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.AlnicoConsole;
import mdteam.ait.tardis.console.SteamConsole;
import net.minecraft.util.Identifier;

public class SteamVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/steam");

    public SteamVariant() {
        super(SteamConsole.REFERENCE, REFERENCE);
    }
}
