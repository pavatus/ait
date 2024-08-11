package loqor.ait.tardis.exterior.variant.box.client;

import org.joml.Vector3f;

import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.data.BiomeHandler;

public class ClientPoliceBoxTokamakVariant extends ClientPoliceBoxVariant {

    private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientPoliceBoxVariant.OVERRIDES)
            .with(type -> type.getTexture(this.texture()), BiomeHandler.BiomeType.SNOWY, BiomeHandler.BiomeType.CHORUS,
                    BiomeHandler.BiomeType.SCULK)
            .build();

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
