package loqor.ait.data.schema.exterior.variant.present.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PresentExteriorModel;
import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;

public abstract class ClientPresentVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/present";
    protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID,
            CATEGORY_PATH + "/present.png");
    protected static final String TEXTURE_PATH = CATEGORY_PATH + "/present_";

    protected static final BiomeOverrides OVERRIDES = BiomeOverrides.of(type -> type.getTexture(CATEGORY_IDENTIFIER));

    protected ClientPresentVariant(String name) {
        super(AITMod.id("exterior/present/" + name));

        this.name = name;
    }

    @Override
    public ExteriorModel model() {
        return new PresentExteriorModel(PresentExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public Identifier texture() {
        return AITMod.id(TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.50f, 2.04f, 1.9f);
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
