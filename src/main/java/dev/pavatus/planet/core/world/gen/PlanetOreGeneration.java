package dev.pavatus.planet.core.world.gen;


import dev.pavatus.planet.core.planet.PlanetBiomeSelectors;
import dev.pavatus.planet.core.world.PlanetPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;

import net.minecraft.world.gen.GenerationStep;


public class PlanetOreGeneration {
    public static void generateOres() {
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_COAL_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_COPPER_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_IRON_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_REDSTONE_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_LAPIS_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_GOLD_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_DIAMOND_ORE_PLACED_KEY);
        BiomeModifications.addFeature(PlanetBiomeSelectors.foundInMars(),
                GenerationStep.Feature.UNDERGROUND_ORES, PlanetPlacedFeatures.MARTIAN_EMERALD_ORE_PLACED_KEY);
    }

}
