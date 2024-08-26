package loqor.ait.tardis.exterior.variant.classic.client;

import org.joml.Vector3f;

import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.handler.BiomeHandler;

public class ClientClassicBoxPtoredVariant extends ClientClassicBoxVariant {

    private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientClassicBoxVariant.OVERRIDES)
            .with(type -> type.getTexture(this.texture()), BiomeHandler.BiomeType.CHERRY, BiomeHandler.BiomeType.CHORUS,
                    BiomeHandler.BiomeType.SNOWY, BiomeHandler.BiomeType.SCULK)
            .build();

    public ClientClassicBoxPtoredVariant() {
        super("ptored");
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.56f, 1.325f, 1.165f);
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
