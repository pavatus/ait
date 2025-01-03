package loqor.ait.data.schema.console.variant.borealis;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.BorealisType;

public class AutumnVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/autumn");

    public AutumnVariant() {
        super(BorealisType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
    }
}
