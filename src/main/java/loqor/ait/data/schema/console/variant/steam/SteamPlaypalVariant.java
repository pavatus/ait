package loqor.ait.data.schema.console.variant.steam;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.type.SteamType;

public class SteamPlaypalVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/steam_playpal");

    public SteamPlaypalVariant() {
        super(SteamType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
