package loqor.ait.data.schema.console.variant.crystalline;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;

public class CrystallineMasterVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/crystalline_master");

    public CrystallineMasterVariant() {
        super(CrystallineMasterVariant.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.PILOT));
    }
}
