package loqor.ait.tardis.console.variant.hartnell;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.HartnellType;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class HartnellVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hartnell");

	public HartnellVariant() {
		super(HartnellType.REFERENCE, REFERENCE);
	}
}
