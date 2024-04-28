package loqor.ait.tardis.console.variant.toyota;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.ToyotaType;
import loqor.ait.tardis.console.variant.ConsoleVariantSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

public class ToyotaLegacyVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/toyota_legacy");

	public ToyotaLegacyVariant() {
		super(ToyotaType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
	}
}
