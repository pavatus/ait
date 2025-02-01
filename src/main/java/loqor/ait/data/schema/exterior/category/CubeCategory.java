package loqor.ait.data.schema.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.exterior.ExteriorCategorySchema;

public class CubeCategory extends ExteriorCategorySchema {
    public static final Identifier REFERENCE = AITMod.id("exterior/cube");

    public CubeCategory() {
        super(REFERENCE, "cube");
    }
}
