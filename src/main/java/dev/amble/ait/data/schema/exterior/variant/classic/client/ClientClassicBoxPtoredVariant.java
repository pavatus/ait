package dev.amble.ait.data.schema.exterior.variant.classic.client;

import org.joml.Vector3f;

import dev.amble.ait.core.tardis.handler.BiomeHandler;
import dev.amble.ait.data.datapack.exterior.BiomeOverrides;

public class ClientClassicBoxPtoredVariant extends ClientClassicBoxVariant {

    private final BiomeOverrides OVERRIDES = BiomeOverrides.builder(ClientClassicBoxVariant.OVERRIDES)
            .with(type -> type.getTexture(BIOME_IDENTIFIER), BiomeHandler.BiomeType.CHERRY, BiomeHandler.BiomeType.CHORUS,
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
