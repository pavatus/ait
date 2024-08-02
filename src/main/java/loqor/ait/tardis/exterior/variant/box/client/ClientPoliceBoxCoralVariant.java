package loqor.ait.tardis.exterior.variant.box.client;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxCoralModel;

public class ClientPoliceBoxCoralVariant extends ClientPoliceBoxVariant {
	public ClientPoliceBoxCoralVariant() {
		super("coral");
	}

	@Override
	public ExteriorModel model() {
		return new PoliceBoxCoralModel(PoliceBoxCoralModel.getTexturedModelData().createModel());
	}
}
