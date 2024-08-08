package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

public class PoliceBoxCategory extends ExteriorCategorySchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/police_box");

	public PoliceBoxCategory() {
		super(REFERENCE, "police_box");
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public ExteriorVariantSchema getDefaultVariant() {
		return ExteriorVariantRegistry.BOX_DEFAULT;
	}
}

