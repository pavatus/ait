package loqor.ait.tardis.console.variant.alnico;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.console.type.AlnicoType;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class BlueAlnicoVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/alnico_blue");

	public BlueAlnicoVariant() {
		super(AlnicoType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.COMPANION));
	}
}
