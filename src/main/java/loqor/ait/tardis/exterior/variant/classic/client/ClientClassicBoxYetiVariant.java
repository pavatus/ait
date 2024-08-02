package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.util.Identifier;

public class ClientClassicBoxYetiVariant extends ClientClassicBoxVariant {
	public ClientClassicBoxYetiVariant() {
		super("yeti");
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
