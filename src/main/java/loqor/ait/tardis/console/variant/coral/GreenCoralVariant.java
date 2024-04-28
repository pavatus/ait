package loqor.ait.tardis.console.variant.coral;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.CoralType;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class GreenCoralVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/coral/green");

	public GreenCoralVariant() {
		super(CoralType.REFERENCE, REFERENCE);
	}
}
