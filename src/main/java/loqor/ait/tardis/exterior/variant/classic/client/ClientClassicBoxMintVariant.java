package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientClassicBoxMintVariant extends ClientClassicBoxVariant {
	public ClientClassicBoxMintVariant() {
		super("mint");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.55f, 1.125f, 1.165f);
	}

	@Override
	public Identifier getBiomeTexture(BiomeHandler.BiomeType biomeType) {
		return switch(biomeType) {
			default -> super.getBiomeTexture(biomeType);
			case SNOWY -> BiomeHandler.BiomeType.SNOWY.getTexture(texture());
			case SCULK -> BiomeHandler.BiomeType.SCULK.getTexture(texture());
			case CHORUS -> BiomeHandler.BiomeType.CHORUS.getTexture(texture());
			case CHERRY -> BiomeHandler.BiomeType.CHERRY.getTexture(texture());
		};
	}
}
