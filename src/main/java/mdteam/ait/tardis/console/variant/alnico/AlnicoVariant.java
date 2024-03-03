package mdteam.ait.tardis.console.variant.alnico;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.AlnicoType;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import net.minecraft.util.Identifier;

public class AlnicoVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/alnico");

	public AlnicoVariant() {
		super(AlnicoType.REFERENCE, REFERENCE);
	}
}
