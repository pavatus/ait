package loqor.ait.core.data.datapack.exterior;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;

import loqor.ait.client.util.PossibleIdentifier;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.util.EnumMap;

public record BiomeOverrides(PossibleIdentifier snowy, PossibleIdentifier sculk, PossibleIdentifier sandy, PossibleIdentifier redSandy,
                             PossibleIdentifier muddy, PossibleIdentifier chorus, PossibleIdentifier cherry) {

    public static BiomeOverrides EMPTY = new BiomeOverrides(null, null, null, null, null, null, null);

    public static BiomeOverrides create(Identifier snowy, Identifier sculk, Identifier sandy, Identifier redSandy,
                                        Identifier muddy, Identifier chorus, Identifier cherry) {
        return new BiomeOverrides(PossibleIdentifier.of(snowy), PossibleIdentifier.of(sculk), PossibleIdentifier.of(sandy), PossibleIdentifier.of(redSandy), PossibleIdentifier.of(muddy), PossibleIdentifier.of(chorus), PossibleIdentifier.of(cherry));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static BiomeOverrides create(Optional<Identifier> snowy, Optional<Identifier> sculk,
            Optional<Identifier> sandy, Optional<Identifier> redSandy, Optional<Identifier> muddy,
            Optional<Identifier> chorus, Optional<Identifier> cherry) {
        return BiomeOverrides.create(snowy.orElse(null), sculk.orElse(null), sandy.orElse(null), redSandy.orElse(null),
                muddy.orElse(null), chorus.orElse(null), cherry.orElse(null));
    }

    public static BiomeOverrides of(Function<BiomeHandler.BiomeType, Identifier> func) {
        return BiomeOverrides.create(func.apply(BiomeHandler.BiomeType.SNOWY), func.apply(BiomeHandler.BiomeType.SCULK),
                func.apply(BiomeHandler.BiomeType.SANDY), func.apply(BiomeHandler.BiomeType.RED_SANDY),
                func.apply(BiomeHandler.BiomeType.MUDDY), func.apply(BiomeHandler.BiomeType.CHORUS),
                func.apply(BiomeHandler.BiomeType.CHERRY));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(BiomeOverrides overrides) {
        return new Builder(overrides);
    }

    public static final Codec<BiomeOverrides> CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(Identifier.CODEC.optionalFieldOf("snowy").forGetter(o -> Optional.ofNullable(o.snowy)),
                            Identifier.CODEC.optionalFieldOf("sculk").forGetter(o -> Optional.ofNullable(o.sculk)),
                            Identifier.CODEC.optionalFieldOf("sandy").forGetter(o -> Optional.ofNullable(o.sandy)),
                            Identifier.CODEC.optionalFieldOf("red_sandy")
                                    .forGetter(o -> Optional.ofNullable(o.redSandy)),
                            Identifier.CODEC.optionalFieldOf("muddy").forGetter(o -> Optional.ofNullable(o.muddy)),
                            Identifier.CODEC.optionalFieldOf("chorus").forGetter(o -> Optional.ofNullable(o.chorus)),
                            Identifier.CODEC.optionalFieldOf("cherry").forGetter(o -> Optional.ofNullable(o.cherry)))
                    .apply(instance, BiomeOverrides::create));

    public static class Builder {

        private final EnumMap<BiomeHandler.BiomeType, Identifier> map = new EnumMap<>(
                () -> BiomeHandler.BiomeType.VALUES, Identifier[]::new);

        private Builder() {
        }

        private Builder(BiomeOverrides overrides) {
            this.with(BiomeHandler.BiomeType.SNOWY, overrides.snowy).with(BiomeHandler.BiomeType.SCULK, overrides.sculk)
                    .with(BiomeHandler.BiomeType.SANDY, overrides.sandy)
                    .with(BiomeHandler.BiomeType.RED_SANDY, overrides.redSandy)
                    .with(BiomeHandler.BiomeType.MUDDY, overrides.muddy)
                    .with(BiomeHandler.BiomeType.CHORUS, overrides.chorus)
                    .with(BiomeHandler.BiomeType.CHERRY, overrides.cherry);
        }

        public Builder with(BiomeHandler.BiomeType type, Identifier id) {
            map.put(type, id);
            return this;
        }

        public Builder with(Function<BiomeHandler.BiomeType, Identifier> func, BiomeHandler.BiomeType... types) {
            for (BiomeHandler.BiomeType type : types) {
                this.with(type, func.apply(type));
            }

            return this;
        }

        public BiomeOverrides build() {
            return BiomeOverrides.create(map.get(BiomeHandler.BiomeType.SNOWY), map.get(BiomeHandler.BiomeType.SCULK),
                    map.get(BiomeHandler.BiomeType.SANDY), map.get(BiomeHandler.BiomeType.RED_SANDY),
                    map.get(BiomeHandler.BiomeType.MUDDY), map.get(BiomeHandler.BiomeType.CHORUS),
                    map.get(BiomeHandler.BiomeType.CHERRY));
        }
    }
}
