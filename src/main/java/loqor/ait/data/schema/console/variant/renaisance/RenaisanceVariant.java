package loqor.ait.data.schema.console.variant.renaisance;

import loqor.ait.data.schema.console.type.RenaisanceType;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;

public class RenaisanceVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/renaisance");

    public RenaisanceVariant() {
        super(RenaisanceType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.PILOT));
    }
}
