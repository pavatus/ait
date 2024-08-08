package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.client.models.exteriors.ClassicHudolinExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;

public class ClientClassicBoxShalkaVariant extends ClientClassicBoxVariant {

	public ClientClassicBoxShalkaVariant() {
		super("shalka");
	}

	@Override
	public ExteriorModel model() {
		return new ClassicHudolinExteriorModel(ClassicHudolinExteriorModel.getTexturedModelData().createModel());
	}
}
