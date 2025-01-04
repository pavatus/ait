package loqor.ait.data.schema.console.variant.hartnell;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.HartnellType;

public class KeltHartnellVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/hartnell_kelt");

    public KeltHartnellVariant() {
        super(HartnellType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
    }
}
