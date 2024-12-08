package dev.pavatus.planet.core.world;

import java.util.List;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import loqor.ait.AITMod;



public class PlanetPlacedFeatures {
    public static final RegistryKey<PlacedFeature> MARTIAN_COAL_ORE_PLACED_KEY = registerKey("martian_coal_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_COPPER_ORE_PLACED_KEY = registerKey("martian_copper_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_IRON_ORE_PLACED_KEY = registerKey("martian_iron_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_GOLD_ORE_PLACED_KEY = registerKey("martian_gold_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_REDSTONE_ORE_PLACED_KEY = registerKey("martian_redstone_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_LAPIS_ORE_PLACED_KEY = registerKey("martian_lapis_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_DIAMOND_ORE_PLACED_KEY = registerKey("martian_diamond_ore_placed");
    public static final RegistryKey<PlacedFeature> MARTIAN_EMERALD_ORE_PLACED_KEY = registerKey("martian_emerald_ore_placed");

    public static void boostrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, MARTIAN_COAL_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_COAL_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_COPPER_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_COAL_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_IRON_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_IRON_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_GOLD_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_GOLD_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_REDSTONE_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_REDSTONE_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_LAPIS_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_LAPIS_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_DIAMOND_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_DIAMOND_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(context, MARTIAN_EMERALD_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(PlanetConfiguredFeatures.MARTIAN_EMERALD_ORE),
                PlanetOrePlacement.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AITMod.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
