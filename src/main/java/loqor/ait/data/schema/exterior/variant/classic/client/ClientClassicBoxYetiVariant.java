package loqor.ait.tardis.exterior.variant.classic.client;

import loqor.ait.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.handler.BiomeHandler;

public class ClientClassicBoxYetiVariant extends ClientClassicBoxVariant {

    private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientClassicBoxVariant.OVERRIDES)
            .with(type -> type.getTexture(this.texture()), BiomeHandler.BiomeType.CHERRY, BiomeHandler.BiomeType.CHORUS,
                    BiomeHandler.BiomeType.SNOWY, BiomeHandler.BiomeType.SCULK)
            .build();

    public ClientClassicBoxYetiVariant() {
        super("yeti");
    }

    @Override
    public BiomeOverrides overrides() {
        return OVERRIDES;
    }
}
