package loqor.ait.core;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import loqor.ait.AITMod;

public class AITDimensions {
    public static final RegistryKey<DimensionType> TARDIS_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(AITMod.MOD_ID, "tardis_dimension"));
    public static final RegistryKey<World> TARDIS_DIM_WORLD = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(AITMod.MOD_ID, "tardis_dimension"));
    public static final RegistryKey<World> TIME_VORTEX_WORLD = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(AITMod.MOD_ID, "time_vortex"));
    public static final RegistryKey<DimensionOptions> TARDIS_DIM_OPTIONS = RegistryKey.of(RegistryKeys.DIMENSION,
            new Identifier(AITMod.MOD_ID, "tardis_dimension"));
    // public static final ResourceKey<DimensionType> TARDIS_DIM_TYPE =
    // ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY,
    // TARDIS_DIM_KEY.registry());
}
