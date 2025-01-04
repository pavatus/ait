package loqor.ait.data.schema.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class DalekModCategory extends ExteriorCategorySchema {
    public static final Identifier REFERENCE = AITMod.id("exterior/dalek_mod");
    public DalekModCategory() {
        super(REFERENCE, "dalek_mod");
    }

    @Override
    public ExteriorVariantSchema getDefaultVariant() {
        return ExteriorVariantRegistry.DALEK_MOD_1963;
    }
}
