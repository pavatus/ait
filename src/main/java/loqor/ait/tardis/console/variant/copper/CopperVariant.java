package loqor.ait.tardis.console.variant.copper;

import loqor.ait.AITMod;
import loqor.ait.tardis.console.type.CopperType;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

public class CopperVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/copper");

	public CopperVariant() {
		super(CopperType.REFERENCE, REFERENCE,new Loyalty(Loyalty.Type.PILOT));
	}
}
