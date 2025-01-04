package loqor.ait.data.schema.console.variant.hudolin;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.HudolinType;

public class HudolinVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/hudolin");

    public HudolinVariant() {
        super(HudolinType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
