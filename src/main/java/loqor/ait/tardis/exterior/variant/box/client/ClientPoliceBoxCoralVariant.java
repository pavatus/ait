package loqor.ait.tardis.exterior.variant.box.client;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxCoralModel;
import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.data.BiomeHandler;

public class ClientPoliceBoxCoralVariant extends ClientPoliceBoxVariant {
	public ClientPoliceBoxCoralVariant() {
		super("coral");
	}

	private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientPoliceBoxVariant.OVERRIDES)
			.with(type -> type.getTexture(this.texture()), BiomeHandler.BiomeType.SANDY)
			.build();

	@Override
	public ExteriorModel model() {
		return new PoliceBoxCoralModel(PoliceBoxCoralModel.getTexturedModelData().createModel());
	}

	@Override
	public BiomeOverrides overrides() {
		return OVERRIDES;
	}
}
