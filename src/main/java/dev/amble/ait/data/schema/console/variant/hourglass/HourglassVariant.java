package dev.amble.ait.data.schema.console.variant.hourglass;

import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.data.Loyalty;
import dev.amble.ait.data.schema.console.ConsoleVariantSchema;
import dev.amble.ait.data.schema.console.type.HourglassType;

public class HourglassVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/hourglass");

    public HourglassVariant() {
        super(HourglassType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.PILOT));
    }
}
