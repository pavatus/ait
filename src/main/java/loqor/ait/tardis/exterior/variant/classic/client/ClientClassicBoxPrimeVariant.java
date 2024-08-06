package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.data.BiomeHandler;

public class ClientClassicBoxPrimeVariant extends ClientClassicBoxVariant {

	private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientClassicBoxVariant.OVERRIDES)
			.with(type -> type.getTexture(this.texture()), BiomeHandler.BiomeType.CHERRY,
					BiomeHandler.BiomeType.CHORUS, BiomeHandler.BiomeType.SNOWY, BiomeHandler.BiomeType.SCULK)
			.build();

	public ClientClassicBoxPrimeVariant() {
		super("prime");
	}

	@Override
	public BiomeOverrides overrides() {
		return OVERRIDES;
	}
}
