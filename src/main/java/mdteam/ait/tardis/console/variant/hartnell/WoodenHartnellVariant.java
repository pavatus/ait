package mdteam.ait.tardis.console.variant.hartnell;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.HartnellType;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class WoodenHartnellVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell_wooden");

    public WoodenHartnellVariant() {
        super(HartnellType.REFERENCE, REFERENCE);
    }
}
