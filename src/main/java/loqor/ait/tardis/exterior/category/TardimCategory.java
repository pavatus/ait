package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

public class TardimCategory extends ExteriorCategorySchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/tardim");

	public TardimCategory() {
		super(REFERENCE, "tardim");
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public ExteriorVariantSchema getDefaultVariant() {
		return ExteriorVariantRegistry.TARDIM_DEFAULT;
	}
}
