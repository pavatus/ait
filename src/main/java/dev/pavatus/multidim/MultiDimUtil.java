package dev.pavatus.multidim;

import dev.pavatus.multidim.api.MutableRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;

import loqor.ait.AITMod;

public class MultiDimUtil {

    public static SimpleRegistry<DimensionOptions> getDimensionsRegistry(MinecraftServer server) {
        DynamicRegistryManager registryManager = server.getCombinedDynamicRegistries().getCombinedRegistryManager();
        return (SimpleRegistry<DimensionOptions>) registryManager.get(RegistryKeys.DIMENSION);
    }

    public static MutableRegistry<DimensionOptions> getMutableDimensionsRegistry(MinecraftServer server) {
        return (MutableRegistry<DimensionOptions>) getDimensionsRegistry(server);
    }
}
