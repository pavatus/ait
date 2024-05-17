package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

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

    public static BiomeType getBiomeTypeFromKey(String biomeKey) {
        return switch(biomeKey) {
            default -> BiomeType.DEFAULT;
            case "snowy_taiga", "snowy_beach" -> BiomeType.SNOWY;
            case "desert", "beach" -> BiomeType.SANDY;
            case "badlands" -> BiomeType.RED_SANDY;
            case "mangrove_swamp" -> BiomeType.MUDDY;
            case "the_end" -> BiomeType.CHORUS;
            case "deep_dark" -> BiomeType.SCULK;
            case "cherry_grove" -> BiomeType.CHERRY;
        };
    }

    public enum BiomeType implements StringIdentifiable {
        DEFAULT(),
        SNOWY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_snowy.png"));
                return specific;
            }
        },
        SCULK() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_sculk.png"));
                return specific;
            }
        },
        SANDY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_sand.png"));
                return specific;
            }
        },
        RED_SANDY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_red_sand.png"));
                return specific;
            }
        },
        MUDDY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_mud.png"));
                return specific;
            }
        },
        CHORUS() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png",  "_chorus.png"));
                return specific;
            }
        },
        CHERRY() {
            @Override
            public Identifier getTextureFromKey(Identifier texture) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_cherry.png"));
                return specific;
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
