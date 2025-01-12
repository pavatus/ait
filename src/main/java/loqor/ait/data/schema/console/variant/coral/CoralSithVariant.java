package loqor.ait.data.schema.console.variant.coral;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.CoralType;

public class CoralSithVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/coral_sith");

    public CoralSithVariant() {
        super(CoralType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.PILOT));
    }
}
