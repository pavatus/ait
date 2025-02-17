package dev.amble.ait.module.planet.core.space.planet;

import dev.amble.lib.register.datapack.SimpleDatapackRegistry;
import org.joml.Vector3f;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

import dev.amble.ait.AITMod;

public class PlanetRegistry extends SimpleDatapackRegistry<Planet> {

    private static final PlanetRegistry instance = new PlanetRegistry();

    public PlanetRegistry() {
        super(Planet::fromInputStream, Planet.CODEC, "planet", true, AITMod.MOD_ID);
    }

    public static Planet OVERWORLD;
    public static Planet THE_NETHER;
    public static Planet THE_END;

    @Override
    protected void defaults() {
        OVERWORLD = register(new Planet(DimensionTypes.OVERWORLD_ID, -1, true, 288,
                new PlanetRenderInfo(
                        AITMod.id("textures/environment/earth.png"),
                        new Vec3d(0, 0, 0),
                        new Vector3f(900f, 900f, 900f),
                        new Vector3f(-22.5f, 45f, 0), true, true,
                        new Vector3f(0.18f, 0.35f, 0.60f),
                        1200,
                        800
                ),
                PlanetTransition.toSpace(1250)
        ));
        THE_NETHER = register(new Planet(DimensionTypes.THE_NETHER_ID, -1, true, 548, PlanetRenderInfo.EMPTY, PlanetTransition.EMPTY));
        THE_END = register(new Planet(DimensionTypes.THE_END_ID, -1, true, 100, PlanetRenderInfo.EMPTY, PlanetTransition.EMPTY));// -1f means dont change gravity btw
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
