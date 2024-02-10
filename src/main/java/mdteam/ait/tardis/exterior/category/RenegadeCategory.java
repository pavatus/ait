package mdteam.ait.tardis.exterior.category;

import mdteam.ait.AITMod;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
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
