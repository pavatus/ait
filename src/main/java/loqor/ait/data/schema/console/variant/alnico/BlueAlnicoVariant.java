package loqor.ait.data.schema.console.variant.alnico;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.AlnicoType;

public class BlueAlnicoVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/alnico_blue");

    public BlueAlnicoVariant() {
        super(AlnicoType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
    }
}
