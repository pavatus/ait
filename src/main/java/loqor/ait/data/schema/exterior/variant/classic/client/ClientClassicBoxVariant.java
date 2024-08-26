package loqor.ait.tardis.exterior.variant.classic.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ClassicExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.tardis.handler.BiomeHandler;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientClassicBoxVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/classic";
    protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID,
            CATEGORY_PATH + "/classic.png");
    protected static final String TEXTURE_PATH = CATEGORY_PATH + "/classic_";

    protected static final BiomeOverrides OVERRIDES = BiomeOverrides.builder()
            .with(type -> type.getTexture(CATEGORY_IDENTIFIER), BiomeHandler.BiomeType.SNOWY,
                    BiomeHandler.BiomeType.SCULK, BiomeHandler.BiomeType.CHORUS, BiomeHandler.BiomeType.CHERRY,
                    BiomeHandler.BiomeType.SANDY)
            .build();

    protected ClientClassicBoxVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/classic/" + name));

        this.name = name;
    }

    @Override
    public ExteriorModel model() {
        return new ClassicExteriorModel(ClassicExteriorModel.getTexturedModelData().createModel());
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
        return new Vector3f(0.42f, 1.125f, 1.165f);
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
