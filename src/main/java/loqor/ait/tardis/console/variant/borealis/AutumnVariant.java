package loqor.ait.tardis.console.variant.borealis;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.BorealisType;
import loqor.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class AutumnVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/autumn");

	public AutumnVariant() {
		super(BorealisType.REFERENCE, REFERENCE);
	}
}

