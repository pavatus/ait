package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import net.minecraft.util.Identifier;

public class DoomCategory extends ExteriorCategorySchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/doom");

	public DoomCategory() {
		super(REFERENCE, "doom");
	}
}
