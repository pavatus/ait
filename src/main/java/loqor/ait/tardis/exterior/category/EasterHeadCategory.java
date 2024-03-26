package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

public class EasterHeadCategory extends ExteriorCategorySchema {

	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/easter_head");

	public EasterHeadCategory() {
		super(REFERENCE, "easter_head");
	}

	@Override
	public ExteriorVariantSchema getDefaultVariant() {
		return ExteriorVariantRegistry.HEAD_DEFAULT;
	}
}
