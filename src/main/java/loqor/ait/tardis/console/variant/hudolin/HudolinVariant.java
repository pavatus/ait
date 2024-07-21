package loqor.ait.tardis.console.variant.hudolin;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.console.type.HudolinType;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

public class HudolinVariant extends ConsoleVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "console/hudolin");

	public HudolinVariant() {
		super(HudolinType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
	}
}
