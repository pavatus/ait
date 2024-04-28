package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

public class RenegadeCategory extends ExteriorCategorySchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/renegade");

	public RenegadeCategory() {
		super(REFERENCE, "renegade");
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public ExteriorVariantSchema getDefaultVariant() {
		return ExteriorVariantRegistry.RENEGADE_DEFAULT;
	}
}
