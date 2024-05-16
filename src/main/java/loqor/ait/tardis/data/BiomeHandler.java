package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class BiomeHandler extends TardisLink {

    public static final String BIOME_KEY = "biome_key";

    public BiomeHandler() {
        super(Id.BIOME);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
    }

    public void setBiome(Tardis tardis) {
        if(tardis.getExterior().getExteriorPos() == null) return;
        World world = tardis.getExterior().getExteriorPos().getWorld();

        if(world.isClient())
            return;

        PropertiesHandler.set(tardis, BIOME_KEY, world.getBiome(tardis.position()).getKey().get().getValue().getPath());
    }

    public String getBiomeKey() {
        return PropertiesHandler.get(this.tardis(), BIOME_KEY);
    }

    public static Identifier biomeTypeFromKey(String biomeKey, Identifier texture, Tardis tardis) {
        return switch(biomeKey) {
            default -> BiomeType.DEFAULT.textureFromKey(texture, tardis);
            case "snowy_taiga", "snowy_beach" -> BiomeType.SNOWY.textureFromKey(texture, tardis);
            case "desert", "beach" -> BiomeType.SANDY.textureFromKey(texture, tardis);
            case "badlands" -> BiomeType.RED_SANDY.textureFromKey(texture, tardis);
            case "mangrove_swamp" -> BiomeType.MUDDY.textureFromKey(texture, tardis);
            case "the_end" -> BiomeType.CHORUS.textureFromKey(texture, tardis);
            case "deep_dark" -> BiomeType.SCULK.textureFromKey(texture, tardis);
            case "cherry_grove" -> BiomeType.CHERRY.textureFromKey(texture, tardis);
        };
    }

    public enum BiomeType implements StringIdentifiable {
        DEFAULT(),
        SNOWY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_snowy.png"));
                return specific;
            }
        },
        SCULK() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_sculk.png"));
                return specific;
            }
        },
        SANDY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_sand.png"));
                return specific;
            }
        },
        RED_SANDY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_red_sand.png"));
                return specific;
            }
        },
        MUDDY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_mud.png"));
                return specific;
            }
        },
        CHORUS() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_chorus.png"));
                return specific;
            }
        },
        CHERRY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_cherry.png"));
                return specific;
            }
        };

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }

        public Identifier textureFromKey(Identifier texture, Tardis tardis) {
            return texture;
        };
    }
}
