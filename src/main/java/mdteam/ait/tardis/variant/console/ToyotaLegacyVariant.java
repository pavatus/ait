package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.ToyotaConsole;
import net.minecraft.util.Identifier;

public class ToyotaLegacyVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/toyota_legacy");

    public ToyotaLegacyVariant() {
        super(ToyotaConsole.REFERENCE, REFERENCE);
    }
}
