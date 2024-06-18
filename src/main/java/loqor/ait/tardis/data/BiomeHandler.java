package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class BiomeHandler extends TardisComponent {

    public static final String BIOME_KEY = "biome_key";

    public BiomeHandler() {
        super(Id.BIOME);
    }

    public void update() {
        if (!(tardis.travel().getPosition() instanceof DirectedGlobalPos.Cached cached))
            return;

        BlockPos pos = cached.getPos();
        World world = cached.getWorld();

        PropertiesHandler.set(tardis, BIOME_KEY, world.getBiome(pos).getKey().get().getValue().getPath());
    }

    public String getBiomeKey() {
        return PropertiesHandler.get(this.tardis(), BIOME_KEY);
    }

    public static BiomeType getBiomeTypeFromKey(String biomeKey) {
        return switch(biomeKey) {
            default -> BiomeType.DEFAULT;
            case "snowy_taiga", "snowy_beach", "frozen_peaks" -> BiomeType.SNOWY;
            case "desert", "beach" -> BiomeType.SANDY;
            case "badlands" -> BiomeType.RED_SANDY;
            case "mangrove_swamp" -> BiomeType.MUDDY;
            case "the_end" -> BiomeType.CHORUS;
            case "deep_dark" -> BiomeType.SCULK;
            case "cherry_grove" -> BiomeType.CHERRY;
        };
    }

    // TODO: replace "replace" with "substring"
    public enum BiomeType implements StringIdentifiable {
        DEFAULT(),
        SNOWY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_snowy.png"));
            }
        },
        SCULK() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_sculk.png"));
            }
        },
        SANDY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_sand.png"));
            }
        },
        RED_SANDY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_red_sand.png"));
            }
        },
        MUDDY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_mud.png"));
            }
        },
        CHORUS() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_chorus.png"));
            }
        },
        CHERRY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_cherry.png"));
            }
        };

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }

        public Identifier getTextureFromKey(Identifier texture) {
            return texture;
        };
    }
}
