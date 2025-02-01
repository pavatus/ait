package loqor.ait.data.schema.console.variant.renaissance;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.RenaissanceType;

public class RenaissanceTokamakVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/renaissance_tokamak");

    public RenaissanceTokamakVariant() {super(RenaissanceType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
