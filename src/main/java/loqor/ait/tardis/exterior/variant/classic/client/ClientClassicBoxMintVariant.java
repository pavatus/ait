package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.data.BiomeHandler;
import org.joml.Vector3f;

public class ClientClassicBoxMintVariant extends ClientClassicBoxVariant {

	private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientClassicBoxVariant.OVERRIDES)
			.with(type -> type.getTexture(this.texture()),
					BiomeHandler.BiomeType.CHORUS, BiomeHandler.BiomeType.SNOWY, BiomeHandler.BiomeType.SCULK)
			.build();

	public ClientClassicBoxMintVariant() {
		super("mint");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.55f, 1.125f, 1.165f);
	}

	@Override
	public BiomeOverrides overrides() {
		return OVERRIDES;
	}
}
