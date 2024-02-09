package mdteam.ait.tardis.console.variant.coral;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.CoralType;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class BlueCoralVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/coral_blue");

    public BlueCoralVariant() {
        super(CoralType.REFERENCE, REFERENCE);
    }
}
