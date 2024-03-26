package loqor.ait.tardis.exterior.category;

import loqor.ait.AITMod;
import net.minecraft.util.Identifier;

public class GrowthCategory extends ExteriorCategorySchema {

	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/coral_growth");

	public GrowthCategory() {
		super(REFERENCE, "coral_growth");
	}

	@Override
	public boolean hasPortals() {
		return true;
	}
}
