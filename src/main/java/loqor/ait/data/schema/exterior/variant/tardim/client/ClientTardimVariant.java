package loqor.ait.tardis.exterior.variant.tardim.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.TardimExteriorModel;
import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientTardimVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/tardim";
    protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID,
            CATEGORY_PATH + "/tardim.png");
    protected static final String TEXTURE_PATH = CATEGORY_PATH + "/tardim_";

    protected static final BiomeOverrides OVERRIDES = BiomeOverrides.of(type -> type.getTexture(CATEGORY_IDENTIFIER));

    protected ClientTardimVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/tardim/" + name));

        this.name = name;
    }

    @Override
    public ExteriorModel model() {
        return new TardimExteriorModel(TardimExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + "_emission" + ".png");
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.53f, 0.94f, 1.2f);
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
