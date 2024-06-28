package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.StringUtils;

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
        this.update(this.tardis.travel2().position());
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
        if (biome.isIn(ConventionalBiomeTags.SNOWY) || biome.isIn(ConventionalBiomeTags.SNOWY_PLAINS) || biome.isIn(ConventionalBiomeTags.ICY))
            return BiomeType.SNOWY;

        if (biome.isIn(ConventionalBiomeTags.DESERT) || biome.isIn(ConventionalBiomeTags.BEACH) || biome.isIn(ConventionalBiomeTags.DEAD))
            return BiomeType.SANDY;

        if (biome.isIn(ConventionalBiomeTags.BADLANDS))
            return BiomeType.RED_SANDY;

        if (biome.isIn(ConventionalBiomeTags.SWAMP))
            return BiomeType.MUDDY;

        if (biome.isIn(ConventionalBiomeTags.IN_THE_END))
            return BiomeType.CHORUS;

        if (biome.isIn(ConventionalBiomeTags.FLORAL))
            return BiomeType.CHERRY;

        return getBiomeTypeFromKey(biome.getKey().get().getValue().getPath());
    }

    // TODO: make tags for these
    private static BiomeType getBiomeTypeFromKey(String biomeKey) {
        return switch(biomeKey) {
            default -> BiomeType.DEFAULT;
            case "deep_dark" -> BiomeType.SCULK;
            case "cherry_grove" -> BiomeType.CHERRY;
        };
    }

    public enum BiomeType implements StringIdentifiable {
        DEFAULT,
        SNOWY("_snowy"),
        SCULK("_sculk"),
        SANDY("_sand"),
        RED_SANDY("_red_sand"),
        MUDDY("_mud"),
        CHORUS("_chorus"),
        CHERRY("_cherry");

        private final String suffix;

        BiomeType(String suffix) {
            this.suffix = suffix;
        }

        BiomeType() {
            this(null);
        }

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }

        public Identifier getTextureFromKey(Identifier texture) {
            if (this.suffix == null)
                return texture;

            String path = texture.getPath();

            return new Identifier(AITMod.MOD_ID, path.substring(
                    0, path.length() - 4) + this.suffix + ".png");
        };
    }
}
