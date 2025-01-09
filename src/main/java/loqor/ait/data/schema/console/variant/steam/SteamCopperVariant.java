package loqor.ait.data.schema.console.variant.steam;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.SteamType;

public class SteamCopperVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/steam_copper");

    public SteamCopperVariant() {
        super(SteamType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.PILOT));
    }
}
