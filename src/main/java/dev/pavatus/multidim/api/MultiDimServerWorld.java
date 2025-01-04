package dev.pavatus.multidim.api;

import java.util.List;
import java.util.concurrent.Executor;

import org.jetbrains.annotations.Nullable;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;

public class MultiDimServerWorld extends ServerWorld {

    private final WorldBlueprint blueprint;

    public MultiDimServerWorld(WorldBlueprint blueprint, MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, List<Spawner> spawners, @Nullable RandomSequencesState randomSequencesState, boolean created) {
        super(server, workerExecutor, session, properties, worldKey, dimensionOptions, worldGenerationProgressListener, false, blueprint.seed(), spawners, blueprint.shouldTickTime(), randomSequencesState);

        this.blueprint = blueprint;
    }

    public WorldBlueprint getBlueprint() {
        return blueprint;
    }
}
