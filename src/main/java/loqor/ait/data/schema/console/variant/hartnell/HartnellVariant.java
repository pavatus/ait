package loqor.ait.data.schema.console.variant.hartnell;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.HartnellType;

public class HartnellVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell");

    public HartnellVariant() {
        super(HartnellType.REFERENCE, REFERENCE);
    }
}
