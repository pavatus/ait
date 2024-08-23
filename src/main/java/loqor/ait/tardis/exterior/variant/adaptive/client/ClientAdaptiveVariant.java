package loqor.ait.tardis.exterior.variant.adaptive.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.CapsuleExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;

public class ClientAdaptiveVariant extends ClientExteriorVariantSchema {

    protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/adaptive";

    protected static final BiomeOverrides OVERRIDES = BiomeOverrides.EMPTY;

    public ClientAdaptiveVariant() {
        super(new Identifier(AITMod.MOD_ID, "exterior/adaptive"));
    }

    @Override
    public ExteriorModel model() {
        return new CapsuleExteriorModel(CapsuleExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, CATEGORY_PATH + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.5f, 1.2f, 1.15f);
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
