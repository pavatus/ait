package loqor.ait.tardis.exterior.variant.box.client;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxCoralModel;
import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import static loqor.ait.tardis.data.BiomeHandler.BiomeType.SNOWY;

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
			case SNOWY -> BiomeHandler.BiomeType.SNOWY.getTextureFromKey(texture());
			case SCULK -> BiomeHandler.BiomeType.SCULK.getTextureFromKey(texture());
			case SANDY -> BiomeHandler.BiomeType.SANDY.getTextureFromKey(texture());
			case RED_SANDY -> BiomeHandler.BiomeType.RED_SANDY.getTextureFromKey(texture());
			case MUDDY -> BiomeHandler.BiomeType.MUDDY.getTextureFromKey(texture());
			case CHORUS -> BiomeHandler.BiomeType.CHORUS.getTextureFromKey(texture());
			case CHERRY -> BiomeHandler.BiomeType.CHERRY.getTextureFromKey(texture());
		};
	}
}
