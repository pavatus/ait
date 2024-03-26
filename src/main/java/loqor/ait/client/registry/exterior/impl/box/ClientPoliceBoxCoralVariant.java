package loqor.ait.client.registry.exterior.impl.box;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxCoralModel;
import org.joml.Vector3f;

public class ClientPoliceBoxCoralVariant extends ClientPoliceBoxVariant {
	public ClientPoliceBoxCoralVariant() {
		super("coral");
	}

	@Override
	public ExteriorModel model() {
		return new PoliceBoxCoralModel(PoliceBoxCoralModel.getTexturedModelData().createModel());
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.56f, 1.2f, 1.2f);
	}
}
