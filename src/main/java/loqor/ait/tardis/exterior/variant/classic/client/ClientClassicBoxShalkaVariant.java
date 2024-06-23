package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.client.models.exteriors.ClassicHudolinExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.util.Identifier;

public class ClientClassicBoxShalkaVariant extends ClientClassicBoxVariant {
	public ClientClassicBoxShalkaVariant() {
		super("shalka");
	}

	@Override
	public ExteriorModel model() {
		return new ClassicHudolinExteriorModel(ClassicHudolinExteriorModel.getTexturedModelData().createModel());
	}

	@Override
	public Identifier getBiomeTexture(BiomeHandler.BiomeType biomeType) {
		return switch(biomeType) {
			default -> super.getBiomeTexture(biomeType);
			case SNOWY -> BiomeHandler.BiomeType.SNOWY.getTextureFromKey(texture());
			case SCULK -> BiomeHandler.BiomeType.SCULK.getTextureFromKey(texture());
			case CHORUS -> BiomeHandler.BiomeType.CHORUS.getTextureFromKey(texture());
			case CHERRY -> BiomeHandler.BiomeType.CHERRY.getTextureFromKey(texture());
		};
	}
}
