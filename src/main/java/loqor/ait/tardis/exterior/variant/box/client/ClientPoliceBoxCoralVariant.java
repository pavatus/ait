package loqor.ait.tardis.exterior.variant.box.client;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxCoralModel;
import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.util.Identifier;
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

	@Override
	public Identifier getBiomeTexture(BiomeHandler.BiomeType biomeType) {
		return switch(biomeType) {
			default -> super.getBiomeTexture(biomeType);
			case SNOWY -> BiomeHandler.BiomeType.SNOWY.getTexture(texture());
			case SCULK -> BiomeHandler.BiomeType.SCULK.getTexture(texture());
			case SANDY -> BiomeHandler.BiomeType.SANDY.getTexture(texture());
			case RED_SANDY -> BiomeHandler.BiomeType.RED_SANDY.getTexture(texture());
			case MUDDY -> BiomeHandler.BiomeType.MUDDY.getTexture(texture());
			case CHORUS -> BiomeHandler.BiomeType.CHORUS.getTexture(texture());
			case CHERRY -> BiomeHandler.BiomeType.CHERRY.getTexture(texture());
		};
	}
}
