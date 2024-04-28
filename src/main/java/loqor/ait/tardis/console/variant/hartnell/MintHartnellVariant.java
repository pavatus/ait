package loqor.ait.tardis.console.variant.hartnell;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.HartnellType;
import loqor.ait.tardis.console.variant.ConsoleVariantSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

public class MintHartnellVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell_mint");

	public MintHartnellVariant() {
		super(HartnellType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.PILOT));
	}
}
