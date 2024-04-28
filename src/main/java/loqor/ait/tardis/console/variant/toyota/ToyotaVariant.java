package loqor.ait.tardis.console.variant.toyota;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.ToyotaType;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

public class ToyotaVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/toyota");

	public ToyotaVariant() {
		super(ToyotaType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
	}
}
