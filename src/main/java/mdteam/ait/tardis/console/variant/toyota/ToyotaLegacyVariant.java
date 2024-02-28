package mdteam.ait.tardis.console.variant.toyota;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.ToyotaType;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class ToyotaLegacyVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/toyota_legacy");

	public ToyotaLegacyVariant() {
		super(ToyotaType.REFERENCE, REFERENCE);
	}
}
