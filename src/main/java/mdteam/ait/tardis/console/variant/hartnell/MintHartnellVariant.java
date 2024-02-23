package mdteam.ait.tardis.console.variant.hartnell;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.HartnellType;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class MintHartnellVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnelL_mint");

    public MintHartnellVariant() {
        super(HartnellType.REFERENCE, REFERENCE);
    }
}
