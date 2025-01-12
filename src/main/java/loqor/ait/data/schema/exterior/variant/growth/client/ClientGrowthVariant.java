package loqor.ait.data.schema.exterior.variant.growth.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.coral.CoralGrowthExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.renderers.coral.CoralRenderer;
import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;

public class ClientGrowthVariant extends ClientExteriorVariantSchema {

    public ClientGrowthVariant() {
        super(AITMod.id("exterior/coral_growth"));
    }

    @Override
    public ExteriorModel model() {
        return new CoralGrowthExteriorModel(CoralGrowthExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public Identifier texture() {
        return CoralRenderer.CORAL_GROWTH_TEXTURE;
    }

    @Override
    public Identifier emission() {
        return null;
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0, 0, 0);
    }

    @Override
    public BiomeOverrides overrides() {
        return null;
    }
}
