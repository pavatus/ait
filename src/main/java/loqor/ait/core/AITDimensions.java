package loqor.ait.core;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

import loqor.ait.AITMod;

public class AITDimensions {
    public static final RegistryKey<World> TIME_VORTEX_WORLD = RegistryKey.of(RegistryKeys.WORLD,
            AITMod.id("time_vortex"));

    public static final RegistryKey<World> MARS = RegistryKey.of(RegistryKeys.WORLD,
            AITMod.id("mars"));
    public static final RegistryKey<World> MOON = RegistryKey.of(RegistryKeys.WORLD,
            AITMod.id("moon"));
    public static final RegistryKey<World> SPACE = RegistryKey.of(RegistryKeys.WORLD,
            AITMod.id("space"));
}
