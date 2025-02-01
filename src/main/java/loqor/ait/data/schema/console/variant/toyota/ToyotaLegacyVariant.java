package loqor.ait.data.schema.console.variant.toyota;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.ToyotaType;

public class ToyotaLegacyVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/toyota_legacy");

    public ToyotaLegacyVariant() {
        super(ToyotaType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
