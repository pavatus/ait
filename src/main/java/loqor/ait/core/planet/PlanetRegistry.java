package loqor.ait.core.planet;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

import loqor.ait.core.AITDimensions;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;

public class PlanetRegistry extends SimpleDatapackRegistry<Planet> {
    private static final PlanetRegistry instance = new PlanetRegistry();


    public PlanetRegistry() {
        super(Planet::fromInputStream, Planet.CODEC, "planet", true);
    }

    public static Planet OVERWORLD;
    @Override
    protected void defaults() {
        OVERWORLD = register(new Planet(DimensionTypes.OVERWORLD_ID, -1, true, 288)); // -1f means dont change gravity btw

        register(new Planet(AITDimensions.MARS.getValue(), 0.05f, false, 210)); // todo - move mars to datapack
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
