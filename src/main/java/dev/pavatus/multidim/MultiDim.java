package dev.pavatus.multidim;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.mojang.serialization.Lifecycle;
import dev.pavatus.multidim.api.MultiDimServer;
import dev.pavatus.multidim.api.MutableRegistry;
import dev.pavatus.multidim.api.WorldBuilder;
import dev.pavatus.multidim.impl.SimpleWorldProgressListener;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

import loqor.ait.AITMod;

public class MultiDim implements ModInitializer {

    private static MultiDim instance;
    public static final Logger LOGGER = LoggerFactory.getLogger("ait-multidim");

    static {
        ServerTickEvents.START_SERVER_TICK.register(server -> MultiDim.get(server).tick());
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> MultiDim.get(server).onServerStopping());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> MultiDim.get(server).onServerStarting());
    }

    public static MultiDim get(MinecraftServer server) {
        if (instance == null || instance.server != server)
            instance = new MultiDim(server);

        return instance;
    }

    private final MinecraftServer server;
    private final MultiDimFileManager files;

    private final Set<ServerWorld> toDelete = new ReferenceOpenHashSet<>();
    private final Set<ServerWorld> toUnload = new ReferenceOpenHashSet<>();

    private MultiDim(MinecraftServer server) {
        this.server = server;
        this.files = new MultiDimFileManager();
    }

    /**
     * dont use! is for fabric init only
     */
    public MultiDim() {
        this.server = null;
        this.files = null;
    }

    @SuppressWarnings("resource")
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
        ServerWorld existing = this.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, builder.id()));
        if (existing != null) return existing;

        MutableRegistry<DimensionOptions> dimensionsRegistry = MultiDimUtil.getMutableDimensionsRegistry(this.server);

        boolean wasFrozen = dimensionsRegistry.multidim$isFrozen();

        if (wasFrozen)
            dimensionsRegistry.multidim$unfreeze();

        DimensionOptions options = builder.buildOptions(this.server);
        RegistryKey<DimensionOptions> key = RegistryKey.of(RegistryKeys.DIMENSION, options.dimensionTypeEntry()
                .getKey().map(RegistryKey::getValue).orElse(builder.id()));

        if (!dimensionsRegistry.multidim$contains(key))
            dimensionsRegistry.multidim$add(key, options, Lifecycle.stable());

        if (wasFrozen)
            dimensionsRegistry.multidim$freeze();

        ServerWorld world = builder.build(this.server, options);
        this.load(world);

        if (builder.loadOnStartup()) {
            this.files.add(world);
        }

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

    public void removeLater(ServerWorld world) {
        this.server.submit(() -> this.toDelete.add(world));
    }

    public void unloadLater(ServerWorld world) {
        this.server.submit(() -> this.toUnload.add(world));
    }

    private void tick() {
        Set<ServerWorld> deletionQueue = this.toDelete;

        if (!deletionQueue.isEmpty())
            deletionQueue.removeIf(this::tickDeleteWorld);

        Set<ServerWorld> unloadingQueue = this.toUnload;

        if (!unloadingQueue.isEmpty())
            unloadingQueue.removeIf(this::tickUnloadWorld);
    }

    private boolean prepareForUnload(ServerWorld world) {
        if (this.isWorldUnloaded(world)) {
            return true;
        }

        this.kickPlayers(world);
        return false;
    }

    private boolean tickDeleteWorld(ServerWorld world) {
        if (!this.prepareForUnload(world))
            return false;

        this.remove(world.getRegistryKey());
        return true;
    }

    private boolean tickUnloadWorld(ServerWorld world) {
        if (!this.prepareForUnload(world))
            return false;

        this.unload(world.getRegistryKey());
        return true;
    }

    private void load(ServerWorld world) {
        AITMod.LOGGER.info("Loading world {}", world.getRegistryKey().getValue());

        if (((MultiDimServer) this.server).multidim$hasWorld(world.getRegistryKey())) {
            AITMod.LOGGER.warn("World {} is already loaded", world.getRegistryKey().getValue());
            return;
        }

        ((MultiDimServer) this.server).multidim$addWorld(world);

        ServerWorldEvents.LOAD.invoker().onWorldLoad(this.server, world);
        world.tick(() -> true);
    }
    public boolean isWorldUnloaded(ServerWorld world) {
        return world.getPlayers().isEmpty() && world.getChunkManager().getLoadedChunkCount() <= 0;
    }

    public void kickPlayers(ServerWorld world) {
        if (world.getPlayers().isEmpty())
            return;

        ServerWorld overworld = this.server.getOverworld();
        Vec3d spawnPos = overworld.getSpawnPos().toCenterPos();

        for (ServerPlayerEntity player : world.getPlayers()) {
            player.teleport(overworld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYaw(), player.getPitch());
        }
    }

    private void onServerStopping() {
        this.files.write(this.server);
    }
    private void onServerStarting() {
        HashMap<RegistryKey<World>, DimensionOptions> map = this.files.read(this.server);

        // load all worlds
        for (RegistryKey<World> key : map.keySet()) {
            this.add(new WorldBuilder(key.getValue()).withOptions(map.get(key)));
        }
    }

    @Override
    public void onInitialize() {

    }
}
