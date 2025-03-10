package dev.amble.ait.registry.v2;

import dev.amble.ait.AITMod;
import dev.amble.ait.data.datapack.DatapackCategory;
import dev.amble.ait.data.datapack.DatapackExterior;
import dev.amble.ait.data.schema.exterior.ExteriorCategorySchema;
import dev.amble.lib.registry.DatapackAmbleRegistry;

public class ExteriorVariantRegistry extends DatapackAmbleRegistry<DatapackExterior> {

    public ExteriorVariantRegistry() {
        super(AITMod.id("exterior"), DatapackExterior.CODEC);
    }
}
