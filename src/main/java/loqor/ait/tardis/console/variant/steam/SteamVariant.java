package loqor.ait.tardis.console.variant.steam;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.console.type.SteamType;
import loqor.ait.tardis.data.loyalty.Loyalty;

public class SteamVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/steam");

    public SteamVariant() {
        super(SteamType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
    }
}
