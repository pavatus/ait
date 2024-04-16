package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.structure.OceanRuinStructure;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class BiomeHandler extends TardisLink {

    public static final String BIOME_KEY = "snowy";

    public BiomeHandler(Tardis tardis) {
        super(tardis, TypeId.BIOME);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if(this.findTardis().isEmpty()) return;
        this.setBiome(this.findTardis().get());
    }

    public void setBiome(Tardis tardis) {
        if(tardis.getExterior().getExteriorPos() == null) return;
        World world = tardis.getExterior().getExteriorPos().getWorld();
        if(world.isClient()) return;
        PropertiesHandler.set(tardis,
                BIOME_KEY,
                world.getBiome(tardis.position()).getKey().get().getValue().getPath());
    }

    public String getBiomeKey() {
        if(this.findTardis().isEmpty()) return null;
        return PropertiesHandler.get(this.findTardis().get(), BIOME_KEY);
    }

    public static Identifier biomeTypeFromKey(String biomeKey, Identifier texture) {
        return switch(biomeKey) {
            default -> BiomeType.DEFAULT.textureFromKey(texture);
            case "snowy_taiga" -> BiomeType.SNOWY.textureFromKey(texture);
            case "desert" -> BiomeType.SANDY.textureFromKey(texture);
            case "mangrove_swamp" -> BiomeType.MUDDY.textureFromKey(texture);
            case "the_end" -> BiomeType.CHORUS.textureFromKey(texture);
            case "deep_dark" -> BiomeType.SCULK.textureFromKey(texture);
            case "cherry_grove" -> BiomeType.CHERRY.textureFromKey(texture);
        };
    }

    public enum BiomeType implements StringIdentifiable {
        DEFAULT(),
        SNOWY() {
            @Override
            public Identifier textureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID, texture.getPath().replace(".png", "_snowy.png"));
            }
        },
        SCULK() {
            @Override
            public Identifier textureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID, texture.getPath().replace(".png", "_sculk.png"));
            }
        },
        SANDY() {
            @Override
            public Identifier textureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID, texture.getPath().replace(".png", "_sand.png"));
            }
        },
        MUDDY() {
            @Override
            public Identifier textureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID, texture.getPath().replace(".png", "_mud.png"));
            }
        },
        CHORUS() {
            @Override
            public Identifier textureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID, texture.getPath().replace(".png", "_chorus.png"));
            }
        },
        CHERRY() {
            @Override
            public Identifier textureFromKey(Identifier texture) {
                return new Identifier(AITMod.MOD_ID, texture.getPath().replace(".png", "_cherry.png"));
            }
        };

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }

        public Identifier textureFromKey(Identifier texture) {
            return texture;
        };
    }
}
