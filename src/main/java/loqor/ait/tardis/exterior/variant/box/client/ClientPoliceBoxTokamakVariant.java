package loqor.ait.tardis.exterior.variant.box.client;

import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.data.BiomeHandler;
import org.joml.Vector3f;

public class ClientPoliceBoxTokamakVariant extends ClientPoliceBoxVariant {

	private final BiomeOverrides OVERRIDES = BiomeOverrides.builder()
			.with(type -> type.getTexture(CATEGORY_IDENTIFIER), BiomeHandler.BiomeType.CHERRY,
					BiomeHandler.BiomeType.SANDY, BiomeHandler.BiomeType.RED_SANDY
			).with(type -> type.getTexture(this.texture()), BiomeHandler.BiomeType.CHORUS,
					BiomeHandler.BiomeType.SNOWY
			).build();

	public ClientPoliceBoxTokamakVariant() {
		super("tokamak");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.56f, 1.45f, 1.2f);
	}

	@Override
	public BiomeOverrides overrides() {
		return OVERRIDES;
	}
}
