package loqor.ait.registry.impl;

import java.util.Random;

import loqor.ait.AITMod;
import loqor.ait.data.datapack.DatapackCategory;
import loqor.ait.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.data.schema.exterior.category.*;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;

public class CategoryRegistry extends SimpleDatapackRegistry<ExteriorCategorySchema> {

    private static CategoryRegistry INSTANCE;

    protected CategoryRegistry() {
        super(DatapackCategory::fromInputStream, DatapackCategory.CODEC, "categories", true);
    }

    @Override
    public ExteriorCategorySchema fallback() {
        return CAPSULE;
    }

    @Override
    public ExteriorCategorySchema getRandom(Random random) {
        ExteriorCategorySchema schema = super.getRandom(random);

        return schema == CategoryRegistry.CORAL_GROWTH ? CategoryRegistry.TARDIM : schema;
    }

    public static CategoryRegistry getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.info("CategoryRegistry was not initialized, Creating a new instance");
            INSTANCE = new CategoryRegistry();
        }

        return INSTANCE;
    }

    public static ExteriorCategorySchema CLASSIC;
    public static ExteriorCategorySchema CAPSULE;
    public static ExteriorCategorySchema POLICE_BOX;
    public static ExteriorCategorySchema TARDIM;
    public static ExteriorCategorySchema BOOTH;
    public static ExteriorCategorySchema EASTER_HEAD;
    public static ExteriorCategorySchema CORAL_GROWTH;
    public static ExteriorCategorySchema DOOM;
    public static ExteriorCategorySchema PLINTH;
    public static ExteriorCategorySchema RENEGADE;
    public static ExteriorCategorySchema BOOKSHELF;
    public static ExteriorCategorySchema GEOMETRIC;
    public static ExteriorCategorySchema STALLION;
    public static ExteriorCategorySchema ADAPTIVE;

    @Override
    protected void defaults() {
        CLASSIC = register(new ClassicCategory());
        CAPSULE = register(new CapsuleCategory());
        POLICE_BOX = register(new PoliceBoxCategory());
        TARDIM = register(new TardimCategory());
        BOOTH = register(new BoothCategory());
        EASTER_HEAD = register(new EasterHeadCategory());
        CORAL_GROWTH = register(new GrowthCategory());
        DOOM = register(new DoomCategory());
        PLINTH = register(new PlinthCategory());
        RENEGADE = register(new RenegadeCategory());
        BOOKSHELF = register(new BookshelfCategory());
        GEOMETRIC = register(new GeometricCategory());
        STALLION = register(new StallionCategory());
        ADAPTIVE = register(new AdaptiveCategory());
    }
}
