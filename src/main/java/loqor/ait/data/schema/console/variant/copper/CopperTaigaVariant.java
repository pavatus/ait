package loqor.ait.data.schema.console.variant.copper;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.CopperType;

public class CopperTaigaVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/copper_taiga");

    public CopperTaigaVariant() {
        super(CopperType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
