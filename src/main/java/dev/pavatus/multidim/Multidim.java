package dev.pavatus.multidim;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mojang.serialization.Lifecycle;
import dev.pavatus.multidim.api.MultiDimServer;
import dev.pavatus.multidim.api.MultidimServerWorld;
import dev.pavatus.multidim.api.MutableRegistry;
import dev.pavatus.multidim.api.WorldBlueprint;
import dev.pavatus.multidim.impl.SimpleWorldProgressListener;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.apache.commons.io.FileUtils;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

import loqor.ait.AITMod;

public class Multidim {

    private static Multidim instance;

    private final Map<Identifier, WorldBlueprint> blueprints = new HashMap<>();
    private final MinecraftServer server;

    private final Set<ServerWorld> toDelete = new ReferenceOpenHashSet<>();
    private final Set<ServerWorld> toUnload = new ReferenceOpenHashSet<>();

    public static void init() {
        MultidimFileManager.init();

        ServerTickEvents.START_SERVER_TICK.register(server -> Multidim.get(server).tick());
    }

    private Multidim(MinecraftServer server) {
        this.server = server;
    }

    private void tick() {
        Set<ServerWorld> deletionQueue = this.toDelete;

        if (!deletionQueue.isEmpty())
            deletionQueue.removeIf(this::tickDeleteWorld);

        Set<ServerWorld> unloadingQueue = this.toUnload;

        if (!unloadingQueue.isEmpty())
            unloadingQueue.removeIf(this::tickUnloadWorld);
    }

    public boolean isWorldUnloaded(ServerWorld world) {
        return world.getPlayers().isEmpty() && world.getChunkManager().getLoadedChunkCount() <= 0;
    }

    private boolean prepareForUnload(ServerWorld world) {
        if (this.isWorldUnloaded(world))
            return true;

        this.kickPlayers(world);
        return false;
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

    public void register(WorldBlueprint blueprint) {
        this.blueprints.put(blueprint.id(), blueprint);
    }

    public static Multidim get(MinecraftServer server) {
        if (instance == null || instance.server != server)
            instance = new Multidim(server);

        return instance;
    }

    public MultidimServerWorld add(WorldBlueprint blueprint, Identifier id) {
        return addOrLoad(blueprint, id, true);
    }

    public MultidimServerWorld load(WorldBlueprint blueprint, Identifier id) {
        return addOrLoad(blueprint, id, false);
    }

    public MultidimServerWorld addOrLoad(WorldBlueprint blueprint, Identifier id, boolean created) {
        return this.addOrLoad(blueprint, RegistryKey.of(RegistryKeys.WORLD, id), created);
    }

    public MultidimServerWorld add(WorldBlueprint blueprint, RegistryKey<World> id) {
        return addOrLoad(blueprint, id, true);
    }

    public MultidimServerWorld load(WorldBlueprint blueprint, RegistryKey<World> id) {
        return addOrLoad(blueprint, id, false);
    }

    public MultidimServerWorld addOrLoad(WorldBlueprint blueprint, RegistryKey<World> id, boolean created) {
        ServerWorld existing = this.server.getWorld(id);

        if (existing != null)
            return (MultidimServerWorld) existing;

        MutableRegistry<DimensionOptions> dimensionsRegistry = MultiDimUtil.getMutableDimensionsRegistry(this.server);
        boolean wasFrozen = dimensionsRegistry.multidim$isFrozen();

        if (wasFrozen)
            dimensionsRegistry.multidim$unfreeze();

        DimensionOptions options = blueprint.createOptions(this.server);
        RegistryKey<DimensionOptions> key = RegistryKey.of(RegistryKeys.DIMENSION, options.dimensionTypeEntry()
                .getKey().map(RegistryKey::getValue).orElse(blueprint.id()));

        if (!dimensionsRegistry.multidim$contains(key))
            dimensionsRegistry.multidim$add(key, options, Lifecycle.stable());

        if (wasFrozen)
            dimensionsRegistry.multidim$freeze();

        MultidimServerWorld world = blueprint.createWorld(this.server, id, options, created);
        this.load(world);

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

    public void remove(MultidimServerWorld world) {
        this.remove(world.getRegistryKey());
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
            MultidimMod.LOGGER.warn("Failed to delete world directory", e);

            try {
                FileUtils.forceDeleteOnExit(worldDirectory);
            } catch (IOException ignored) { }
        }
    }

    private void load(MultidimServerWorld world) {
        AITMod.LOGGER.info("Loading world {}", world.getRegistryKey().getValue());

        if (((MultiDimServer) this.server).multidim$hasWorld(world.getRegistryKey())) {
            AITMod.LOGGER.warn("World {} is already loaded", world.getRegistryKey().getValue());
            return;
        }

        ((MultiDimServer) this.server).multidim$addWorld(world);

        ServerWorldEvents.LOAD.invoker().onWorldLoad(this.server, world);
        world.tick(() -> true);
    }

    public WorldBlueprint getBlueprint(Identifier id) {
        return blueprints.get(id);
    }
}
