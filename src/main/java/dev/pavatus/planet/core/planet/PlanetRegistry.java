package dev.pavatus.planet.core.planet;

import dev.pavatus.lib.register.datapack.SimpleDatapackRegistry;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class PlanetRegistry extends SimpleDatapackRegistry<Planet> {

    private static final PlanetRegistry instance = new PlanetRegistry();

    public PlanetRegistry() {
        super(Planet::fromInputStream, Planet.CODEC, "planet", true);
    }

    public static Planet OVERWORLD;
    public static Planet THE_NETHER;
    public static Planet THE_END;

    @Override
    protected void defaults() {
        OVERWORLD = register(new Planet(DimensionTypes.OVERWORLD_ID, -1, true, 288, true));
        THE_NETHER = register(new Planet(DimensionTypes.THE_NETHER_ID, -1, true, 548, false));
        THE_END = register(new Planet(DimensionTypes.THE_END_ID, -1, true, 100, false));// -1f means dont change gravity btw
    }

    @Override
    public Planet fallback() {
        return OVERWORLD;
    }

    public Planet get(World world) {
        // all worlds implement PlanetWorld
        if (!(world instanceof PlanetWorld planetWorld))
            return null;

        if (planetWorld.ait_planet$isAPlanet())
            return planetWorld.ait_planet$getPlanet();

        Planet planet = this.get(world.getRegistryKey().getValue());

        planetWorld.ait_planet$setPlanet(planet);
        planetWorld.ait_planet$setIsAPlanet(planet != null);
        return planet;
    }

    public static PlanetRegistry getInstance() {
        return instance;
    }
}
