package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.client.models.exteriors.ClassicHudolinExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;

public class ClientClassicBoxHudolinVariant extends ClientClassicBoxVariant {

	public ClientClassicBoxHudolinVariant() {
		super("hudolin");
	}

	@Override
	public ExteriorModel model() {
		return new ClassicHudolinExteriorModel(ClassicHudolinExteriorModel.getTexturedModelData().createModel());
	}
}
