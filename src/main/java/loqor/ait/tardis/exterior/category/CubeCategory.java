package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import net.minecraft.util.Identifier;

public class CubeCategory extends ExteriorCategorySchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/cube");

	public CubeCategory() {
		super(REFERENCE, "cube");
	}
}
