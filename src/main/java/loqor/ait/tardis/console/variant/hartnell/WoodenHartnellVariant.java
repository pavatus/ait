package loqor.ait.tardis.console.variant.hartnell;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.console.type.HartnellType;
import loqor.ait.tardis.data.loyalty.Loyalty;

public class WoodenHartnellVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell_wooden");

    public WoodenHartnellVariant() {
        super(HartnellType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.NEUTRAL));
    }
}
