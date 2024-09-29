package dev.pavatus.multidim;

import java.io.File;
import java.io.IOException;

import com.mojang.serialization.Lifecycle;
import dev.pavatus.multidim.api.MultiDimServer;
import dev.pavatus.multidim.api.MutableRegistry;
import dev.pavatus.multidim.api.WorldBuilder;
import dev.pavatus.multidim.impl.SimpleWorldProgressListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

public class MultiDim  {

    private static MultiDim instance;
    private static final Logger LOGGER = LoggerFactory.getLogger("ait-multidim");

    public static MultiDim get(MinecraftServer server) {
        if (instance == null || instance.server != server)
            instance = new MultiDim(server);

        return instance;
    }

    private final MinecraftServer server;

    private MultiDim(MinecraftServer server) {
        this.server = server;
    }

    @SuppressWarnings({"resource"})
    public void remove(RegistryKey<World> key) {
        ServerWorld world = ((MultiDimServer) this.server).multidim$removeWorld(key);

        if (world == null)
            return;

        ServerWorldEvents.UNLOAD.invoker().onWorldUnload(this.server, world);
        MultiDimUtil.getMutableDimensionsRegistry(this.server).multidim$remove(key.getValue());

        LevelStorage.Session session = ((MultiDimServer) this.server).multidim$getSession();
        File worldDirectory = session.getWorldDirectory(key).toFile();

        if (!worldDirectory.exists())
            return;

        try {
            FileUtils.deleteDirectory(worldDirectory);
        } catch (IOException e) {
            MultiDim.LOGGER.warn("Failed to delete world directory", e);

            try {
                FileUtils.forceDeleteOnExit(worldDirectory);
            } catch (IOException ignored) { }
        }
    }

    public ServerWorld add(WorldBuilder builder) {
        MutableRegistry<DimensionOptions> dimensionsRegistry = MultiDimUtil.getMutableDimensionsRegistry(this.server);

        boolean wasFrozen = dimensionsRegistry.multidim$isFrozen();

        if (wasFrozen)
            dimensionsRegistry.multidim$unfreeze();

        RegistryKey<DimensionOptions> key = RegistryKey.of(RegistryKeys.DIMENSION, builder.id());

        if (!dimensionsRegistry.contains(key))
            dimensionsRegistry.add(key, builder.buildOptions(), Lifecycle.stable());

        if (wasFrozen)
            dimensionsRegistry.multidim$freeze();

        ServerWorld world = builder.build();
        ((MultiDimServer) this.server).multidim$addWorld(world);

        ServerWorldEvents.LOAD.invoker().onWorldLoad(this.server, world);
        world.tick(() -> true);

        return world;
    }

    @SuppressWarnings("resource")
    public void unload(RegistryKey<World> key) {
        ServerWorld world = ((MultiDimServer) this.server).multidim$removeWorld(key);

        if (world == null)
            return;

        world.save(new SimpleWorldProgressListener(() -> {
            ServerWorldEvents.UNLOAD.invoker().onWorldUnload(this.server, world);
            MultiDimUtil.getMutableDimensionsRegistry(this.server).multidim$remove(key.getValue());
        }), true, false);
    }
}
