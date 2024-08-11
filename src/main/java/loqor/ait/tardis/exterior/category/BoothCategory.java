package loqor.ait.tardis.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class BoothCategory extends ExteriorCategorySchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/booth");

    public BoothCategory() {
        super(REFERENCE, "booth");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }

    @Override
    public ExteriorVariantSchema getDefaultVariant() {
        return ExteriorVariantRegistry.BOOTH_DEFAULT;
    }
}
