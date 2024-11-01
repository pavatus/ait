package loqor.ait.core.planet;

import static net.minecraft.world.dimension.DimensionTypes.THE_NETHER;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

import loqor.ait.registry.datapack.SimpleDatapackRegistry;

public class PlanetRegistry extends SimpleDatapackRegistry<Planet> {
    private static final PlanetRegistry instance = new PlanetRegistry();


    public PlanetRegistry() {
        super(Planet::fromInputStream, Planet.CODEC, "planet", true);
    }

    public static Planet OVERWORLD;
    public static Planet THE_NETHER;

    @Override
    protected void defaults() {
        OVERWORLD = register(new Planet(DimensionTypes.OVERWORLD_ID, -1, true, 288));
        THE_NETHER = register(new Planet(DimensionTypes.THE_NETHER_ID, -1, true, 548)); // -1f means dont change gravity btw
    }

    @Override
    public Planet fallback() {
        return OVERWORLD;
    }

    public Planet get(World world) {
        return this.get(world.getRegistryKey().getValue());
    }

    public static PlanetRegistry getInstance() {
        return instance;
    }
}
