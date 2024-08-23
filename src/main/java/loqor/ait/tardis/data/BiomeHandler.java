package loqor.ait.tardis.data;

import java.util.Map;
import java.util.Set;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.*;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.core.util.FakeStructureWorldAccess;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.util.Ordered;
import loqor.ait.tardis.util.TardisUtil;

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

    public Map<BlockPos, BlockState> testBiome(ServerWorld world, BlockPos pos) {
        long start = System.currentTimeMillis();

        RegistryEntry<Biome> biome = world.getBiome(pos);
        ConfiguredFeature<?, ?> tree = this.findTrees(world, biome);
        FakeStructureWorldAccess access = new FakeStructureWorldAccess(world);

        boolean success = tree.generate(access, world.getChunkManager().getChunkGenerator(), world.random, pos);

        System.out.println("Done in " + (System.currentTimeMillis() - start) + "ms; found structure: " + tree + "; success? " + success);
        return access.getPositions();
    }

    private static final Set<Class<? extends Feature<?>>> TREES = Set.of(
            TreeFeature.class, HugeMushroomFeature.class, HugeFungusFeature.class, DesertWellFeature.class
    );

    private static final Identifier CACTUS = new Identifier(AITMod.MOD_ID, "cactus");

    private ConfiguredFeature<?, ?> findTrees(ServerWorld world, RegistryEntry<Biome> biome) {
        if (this.type.get() == BiomeType.SANDY && TardisUtil.random().nextInt(5) != 0)
            return world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).get(CACTUS);

        for (RegistryEntryList<PlacedFeature> feature : biome.value().getGenerationSettings().getFeatures()) {
            for (RegistryEntry<PlacedFeature> entry : feature) {
                ConfiguredFeature<?, ?> configured = entry.value().feature().value();

                if (isTree(configured, biome)) {
                    return configured;
                } else {
                    for (ConfiguredFeature<?, ?> configuredFeature : configured.config().getDecoratedFeatures().toList()) {
                        if (isTree(configuredFeature, biome))
                            return configuredFeature;
                    }
                }
            }
        }

        return null;
    }

    private static boolean isTree(ConfiguredFeature<?, ?> configured, RegistryEntry<Biome> biome) {
        Feature<?> feature = configured.feature();

        for (Class<?> clazz : TREES) {
            if (clazz.isInstance(feature))
                return true;
        }

        return false;
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
        DEFAULT, SNOWY("_snowy"),
        SCULK("_sculk"),
        SANDY("_sand"),
        RED_SANDY("_red_sand"),
        MUDDY("_mud"),
        CHORUS("_chorus"),
        CHERRY("_cherry");

        public static final BiomeType[] VALUES = BiomeType.values();
        public static final Codec<BiomeType> CODEC = StringIdentifiable.createCodec(() -> VALUES);

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

        public Identifier getTexture(Identifier texture) {
            if (this.suffix == null)
                return texture;

            String path = texture.getPath();
            return new Identifier(AITMod.MOD_ID, path.substring(0, path.length() - 4) + this.suffix + ".png");
        };

        public Identifier get(BiomeOverrides overrides) {
            if (overrides == null)
                return null;

            return overrides.get(this);
        }

        @Override
        public int index() {
            return ordinal();
        }
    }
}
