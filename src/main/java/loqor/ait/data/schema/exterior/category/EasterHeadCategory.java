package loqor.ait.tardis.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

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
