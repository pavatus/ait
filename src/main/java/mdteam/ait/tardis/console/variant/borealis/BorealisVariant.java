package mdteam.ait.tardis.console.variant.borealis;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.BorealisType;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class BorealisVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/borealis");

    public BorealisVariant() {
        super(BorealisType.REFERENCE, REFERENCE);
    }
}
