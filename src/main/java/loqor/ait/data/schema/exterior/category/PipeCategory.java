package loqor.ait.data.schema.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class PipeCategory extends ExteriorCategorySchema {
    public static final Identifier REFERENCE = AITMod.id("exterior/pipe");

    public PipeCategory() {
        super(REFERENCE, "pipe");
    }

    @Override
    public boolean hasPortals() {
        return false;
    }

    @Override
    public ExteriorVariantSchema getDefaultVariant() {
        return ExteriorVariantRegistry.PIPE_DEFAULT;
    }
}
