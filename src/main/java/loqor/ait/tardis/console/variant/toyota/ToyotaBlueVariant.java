package loqor.ait.tardis.console.variant.toyota;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.ToyotaType;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class ToyotaBlueVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/toyota_blue");

	public ToyotaBlueVariant() {
		super(ToyotaType.REFERENCE, REFERENCE);
	}
}
