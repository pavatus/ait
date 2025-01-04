package loqor.ait.data.schema.exterior.category;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class BookshelfCategory extends ExteriorCategorySchema {
    public static final Identifier REFERENCE = AITMod.id("exterior/bookshelf");

    public BookshelfCategory() {
        super(REFERENCE, "bookshelf");
    }

    @Override
    public boolean hasPortals() {
        return true;
    }

    @Override
    public ExteriorVariantSchema getDefaultVariant() {
        return ExteriorVariantRegistry.BOOKSHELF_DEFAULT;
    }
}
