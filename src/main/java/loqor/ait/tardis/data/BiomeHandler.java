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

import java.util.Locale;
import java.util.Optional;

public class BiomeHandler extends TardisLink {

    public static final String BIOME_KEY = "biome_key";

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

    public static Identifier biomeTypeFromKey(String biomeKey, Identifier texture, Tardis tardis) {
        return switch(biomeKey) {
            default -> BiomeType.DEFAULT.textureFromKey(texture, tardis);
            case "snowy_taiga" -> BiomeType.SNOWY.textureFromKey(texture, tardis);
            case "desert" -> BiomeType.SANDY.textureFromKey(texture, tardis);
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
                if(specific.getPath() == null) {
                    return new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/" +
                            tardis.getExterior().getCategory().name().toLowerCase() + "_snowy.png");
                }
                return specific;
            }
        },
        SCULK() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_sculk.png"));
                if(specific.getPath() == null) {
                    return new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/" +
                            tardis.getExterior().getCategory().name().toLowerCase() + "_sculk.png");
                }
                return specific;
            }
        },
        SANDY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_sand.png"));
                if(specific.getPath() == null) {
                    return new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/" +
                            tardis.getExterior().getCategory().name().toLowerCase() + "_sand.png");
                }
                return specific;
            }
        },
        MUDDY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_mud.png"));
                if(specific.getPath() == null) {
                    return new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/" +
                            tardis.getExterior().getCategory().name().toLowerCase() + "_mud.png");
                }
                return specific;
            }
        },
        CHORUS() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_chorus.png"));
                if(specific.getPath() == null) {
                    return new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/" +
                            tardis.getExterior().getCategory().name().toLowerCase() + "_chorus.png");
                }
                return specific;
            }
        },
        CHERRY() {
            @Override
            public Identifier textureFromKey(Identifier texture, Tardis tardis) {
                Identifier specific = new Identifier(AITMod.MOD_ID,
                        texture.getPath().replace(".png", "_cherry.png"));
                if(specific.getPath() == null) {
                    return new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/" +
                            tardis.getExterior().getCategory().name().toLowerCase() + "_cherry.png");
                }
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
