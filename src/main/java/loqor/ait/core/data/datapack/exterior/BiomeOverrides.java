package loqor.ait.core.data.datapack.exterior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import loqor.ait.core.data.datapack.DatapackExterior;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.util.EnumMap;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Function;

public record BiomeOverrides(
        Identifier snowy, Identifier sculk, Identifier sandy,
        Identifier redSandy, Identifier muddy, Identifier chorus,
        Identifier cherry
) {

    public static BiomeOverrides EMPTY = new BiomeOverrides(null, null, null, null, null, null, null);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static BiomeOverrides create(
            Optional<Identifier> snowy, Optional<Identifier> sculk, Optional<Identifier> sandy,
            Optional<Identifier> redSandy, Optional<Identifier> muddy, Optional<Identifier> chorus,
            Optional<Identifier> cherry
    ) {
        return new BiomeOverrides(snowy.orElse(null), sculk.orElse(null), sandy.orElse(null),
                redSandy.orElse(null), muddy.orElse(null), chorus.orElse(null),
                cherry.orElse(null));
    }

    public static BiomeOverrides of(Function<BiomeHandler.BiomeType, Identifier> func) {
        return new BiomeOverrides(
                func.apply(BiomeHandler.BiomeType.SNOWY),
                func.apply(BiomeHandler.BiomeType.SCULK),
                func.apply(BiomeHandler.BiomeType.SANDY),
                func.apply(BiomeHandler.BiomeType.RED_SANDY),
                func.apply(BiomeHandler.BiomeType.MUDDY),
                func.apply(BiomeHandler.BiomeType.CHORUS),
                func.apply(BiomeHandler.BiomeType.CHERRY)
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final Codec<BiomeOverrides> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.optionalFieldOf("snowy").forGetter(o -> Optional.ofNullable(o.snowy)),
                    Identifier.CODEC.optionalFieldOf("sculk").forGetter(o -> Optional.ofNullable(o.snowy)),
                    Identifier.CODEC.optionalFieldOf("sandy").forGetter(o -> Optional.ofNullable(o.snowy)),
                    Identifier.CODEC.optionalFieldOf("red_sandy").forGetter(o -> Optional.ofNullable(o.snowy)),
                    Identifier.CODEC.optionalFieldOf("muddy").forGetter(o -> Optional.ofNullable(o.snowy)),
                    Identifier.CODEC.optionalFieldOf("chorus").forGetter(o -> Optional.ofNullable(o.snowy)),
                    Identifier.CODEC.optionalFieldOf("cherry").forGetter(o -> Optional.ofNullable(o.snowy))
            ).apply(instance, BiomeOverrides::create)
    );

    public static class Builder {

        private final EnumMap<BiomeHandler.BiomeType, Identifier> map = new EnumMap<>(() -> BiomeHandler.BiomeType.VALUES, Identifier[]::new);

        private Builder() {
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
            return new BiomeOverrides(
                    map.get(BiomeHandler.BiomeType.SNOWY),
                    map.get(BiomeHandler.BiomeType.SCULK),
                    map.get(BiomeHandler.BiomeType.SANDY),
                    map.get(BiomeHandler.BiomeType.RED_SANDY),
                    map.get(BiomeHandler.BiomeType.MUDDY),
                    map.get(BiomeHandler.BiomeType.CHORUS),
                    map.get(BiomeHandler.BiomeType.CHERRY)
            );
        }
    }
}
