package dev.amble.ait.data.schema.console.variant.coral;

import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.data.Loyalty;
import dev.amble.ait.data.schema.console.ConsoleVariantSchema;
import dev.amble.ait.data.schema.console.type.CoralType;

public class CoralVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/coral");

    public CoralVariant() {
        super(CoralType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
    }
}
