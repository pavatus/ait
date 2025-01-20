package loqor.ait.data.schema.console.variant.copper;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.CopperType;

public class CopperVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/copper");

    public CopperVariant() {
        super(CopperType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
