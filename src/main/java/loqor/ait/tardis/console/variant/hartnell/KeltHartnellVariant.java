package loqor.ait.tardis.console.variant.hartnell;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.HartnellType;
import loqor.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class KeltHartnellVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell_kelt");

	public KeltHartnellVariant() {
		super(HartnellType.REFERENCE, REFERENCE);
	}
}
