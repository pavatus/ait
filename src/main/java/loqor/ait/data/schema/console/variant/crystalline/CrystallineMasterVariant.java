package loqor.ait.data.schema.console.variant.crystalline;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.CrystallineType;

public class CrystallineMasterVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/crystalline_master");

    public CrystallineMasterVariant() {
        super(CrystallineType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
