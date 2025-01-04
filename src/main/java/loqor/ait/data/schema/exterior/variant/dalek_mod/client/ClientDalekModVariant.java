package loqor.ait.data.schema.exterior.variant.dalek_mod.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.advent.DalekModExteriorModel;
import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;


public abstract class ClientDalekModVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/dalek_mod";
    protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID,
            CATEGORY_PATH + "/dalek_mod.png");
    protected static final String TEXTURE_PATH = CATEGORY_PATH + "/dalek_mod_";

    protected static final BiomeOverrides OVERRIDES = BiomeOverrides.of(type -> type.getTexture(CATEGORY_IDENTIFIER));


    protected ClientDalekModVariant(String name) {
        super(AITMod.id("exterior/dalek_mod/" + name));

        this.name = name;
    }

    @Override
    public ExteriorModel model() {
        return new DalekModExteriorModel(DalekModExteriorModel.getTexturedModelData().createModel());
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
        return new Vector3f(0.5f, 1.2f, 1.15f);
    }

    @Override
    public BiomeOverrides overrides() {
        return null;
    }
}
