package dev.amble.ait.data.schema.exterior.variant.bookshelf.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.client.models.exteriors.BookshelfExteriorModel;
import dev.amble.ait.client.models.exteriors.ExteriorModel;
import dev.amble.ait.data.schema.exterior.ClientExteriorVariantSchema;

public abstract class ClientBookshelfVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/bookshelf";
    protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID,
            CATEGORY_PATH + "/bookshelf.png");
    protected static final String TEXTURE_PATH = CATEGORY_PATH + "/bookshelf_";

    protected ClientBookshelfVariant(String name) {
        super(AITMod.id("exterior/bookshelf/" + name));

        this.name = name;
    }

    @Override
    public ExteriorModel model() {
        return new BookshelfExteriorModel(BookshelfExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public Identifier texture() {
        return AITMod.id(TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return AITMod.id(TEXTURE_PATH + name + "_emission" + ".png");
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.56f, 1.2f, 1.2f);
    }

}
