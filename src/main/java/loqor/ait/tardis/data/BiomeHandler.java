package loqor.ait.tardis.data;

import java.util.function.Function;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import loqor.ait.AITMod;
import loqor.ait.client.util.PossibleIdentifier;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.util.Ordered;

/**
 * @author Loqor TODO reminder to work on this more, making it so you have to
 *         brush off different biomes if you don't just demat/remat + having to
 *         land on the respective blocks / has to snow for it to take effect.
 */
public class BiomeHandler extends KeyedTardisComponent {

    private static final Property<BiomeType> TYPE = Property.forEnum("type", BiomeType.class, BiomeType.DEFAULT);
    private final Value<BiomeType> type = TYPE.create(this);

    public BiomeHandler() {
        super(Id.BIOME);
    }

    @Override
    public void onLoaded() {
        type.of(this, TYPE);
    }

    public void update() {
        this.update(this.tardis.travel().position());
    }

    public void update(DirectedGlobalPos.Cached globalPos) {
        if (globalPos == null)
            return;

        RegistryEntry<Biome> entry = globalPos.getWorld().getBiome(globalPos.getPos());
        BiomeType biome = getTagForBiome(entry);

        this.type.set(biome);
    }

    public BiomeType getBiomeKey() {
        return this.type.get();
    }

    private static BiomeType getTagForBiome(RegistryEntry<Biome> biome) {
        if (biome.isIn(ConventionalBiomeTags.SNOWY) || biome.isIn(ConventionalBiomeTags.SNOWY_PLAINS)
                || biome.isIn(ConventionalBiomeTags.ICY))
            return BiomeType.SNOWY;

        if (biome.isIn(ConventionalBiomeTags.DESERT) || biome.isIn(ConventionalBiomeTags.BEACH)
                || biome.isIn(ConventionalBiomeTags.DEAD))
            return BiomeType.SANDY;

        if (biome.isIn(ConventionalBiomeTags.BADLANDS))
            return BiomeType.RED_SANDY;

        if (biome.isIn(ConventionalBiomeTags.SWAMP))
            return BiomeType.MUDDY;

        if (biome.isIn(ConventionalBiomeTags.IN_THE_END))
            return BiomeType.CHORUS;

        if (biome.isIn(ConventionalBiomeTags.FLORAL))
            return BiomeType.CHERRY;

        RegistryKey<Biome> biomeKey = biome.getKey().orElse(null);

        if (biomeKey == BiomeKeys.DEEP_DARK)
            return BiomeType.SCULK;

        if (biomeKey == BiomeKeys.CHERRY_GROVE)
            return BiomeType.CHERRY;

        return BiomeType.DEFAULT;
    }

    public enum BiomeType implements StringIdentifiable, Ordered {
        DEFAULT, SNOWY("_snowy", BiomeOverrides::snowy), SCULK("_sculk", BiomeOverrides::sculk), SANDY("_sand",
                BiomeOverrides::sandy), RED_SANDY("_red_sand", BiomeOverrides::redSandy), MUDDY("_mud",
                        BiomeOverrides::muddy), CHORUS("_chorus",
                                BiomeOverrides::chorus), CHERRY("_cherry", BiomeOverrides::cherry);

        public static final BiomeType[] VALUES = BiomeType.values();

        private final Function<BiomeOverrides, PossibleIdentifier> func;
        private final String suffix;

        BiomeType(String suffix, Function<BiomeOverrides, PossibleIdentifier> func) {
            this.suffix = suffix;
            this.func = func;
        }

        BiomeType() {
            this(null, o -> PossibleIdentifier.empty());
        }

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }

        public Identifier getTexture(Identifier texture) {
            if (this.suffix == null)
                return texture;

            String path = texture.getPath();

            return new Identifier(AITMod.MOD_ID, path.substring(0, path.length() - 4) + this.suffix + ".png");
        };

        public PossibleIdentifier get(BiomeOverrides overrides) {
            if (overrides == null)
                return PossibleIdentifier.empty();

            return this.func.apply(overrides);
        }

        @Override
        public int index() {
            return ordinal();
        }
    }
}
