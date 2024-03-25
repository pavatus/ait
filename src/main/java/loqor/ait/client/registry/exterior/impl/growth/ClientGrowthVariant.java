package loqor.ait.client.registry.exterior.impl.growth;

import loqor.ait.AITMod;
import loqor.ait.client.models.coral.CoralGrowthExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.registry.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.renderers.coral.CoralRenderer;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientGrowthVariant extends ClientExteriorVariantSchema {
	public ClientGrowthVariant() {
		super(new Identifier(AITMod.MOD_ID, "exterior/coral_growth"));
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
}