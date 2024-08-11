package loqor.ait.tardis.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;

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
