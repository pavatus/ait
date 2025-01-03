package dev.pavatus.multidim.api;

import java.util.List;
import java.util.concurrent.Executor;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import dev.pavatus.multidim.impl.AbstractWorldGenListener;
import org.jetbrains.annotations.Nullable;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;

public class WorldBlueprint {

    private final Identifier id;

    private long seed;
    private boolean tickTime = true;

    private Identifier typeId;
    private DimensionType type;

    private WorldCreator creator = MultidimServerWorld::new;
    private ChunkGenerator generator;

    private boolean persistent = true;
    private DimensionOptions options;

    public WorldBlueprint(Identifier id) {
        this.id = id;
    }

    public WorldBlueprint withSeed(long seed) {
        this.seed = BiomeAccess.hashSeed(seed);
        return this;
    }

    public long seed() {
        return this.seed;
    }

    public WorldBlueprint withCreator(WorldCreator creator) {
        this.creator = creator;
        return this;
    }

    public WorldBlueprint shouldTickTime(boolean tickTime) {
        this.tickTime = tickTime;
        return this;
    }

    public boolean shouldTickTime() {
        return this.tickTime;
    }

    public WorldBlueprint withType(Identifier id) {
        this.typeId = id;
        return this;
    }

    public WorldBlueprint withType(DimensionType type) {
        return this.withType(null, type);
    }

    public WorldBlueprint withType(Identifier id, DimensionType type) {
        this.typeId = id;
        this.type = type;
        return this;
    }

    public WorldBlueprint withGenerator(ChunkGenerator generator) {
        this.generator = generator;
        return this;
    }

    public Identifier id() {
        return this.id;
    }

    public WorldBlueprint setPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    public boolean persistent() {
        return this.persistent;
    }

    public MultidimServerWorld createWorld(MinecraftServer server, RegistryKey<World> key, DimensionOptions options, boolean created) {
        SaveProperties saveProps = server.getSaveProperties();

        return this.creator.create(
                this, server, Util.getMainWorkerExecutor(), ((MultiDimServer) server).multidim$getSession(),
                new UnmodifiableLevelProperties(saveProps, saveProps.getMainWorldProperties()), key, options,
                new AbstractWorldGenListener(), ImmutableList.of(), null, created
        );
    }

    private RegistryEntry<DimensionType> resolveType(MinecraftServer server) {
        SimpleRegistry<DimensionType> typeRegistry = (SimpleRegistry<DimensionType>) server.getRegistryManager().get(RegistryKeys.DIMENSION_TYPE);

        if (this.typeId == null)
            this.typeId = this.id;

        RegistryKey<DimensionType> typeKey = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, this.typeId);

        if (this.type == null) {
            RegistryEntry<DimensionType> entry = typeRegistry.getEntry(typeKey).orElse(null);

            if (entry == null)
                return null;

            this.type = entry.value();
            return entry;
        }

        if (!typeRegistry.contains(typeKey))
            return typeRegistry.add(typeKey, this.type, Lifecycle.stable());

        return typeRegistry.getEntry(typeKey).orElse(null);
    }

    public DimensionOptions createOptions(MinecraftServer server) {
        if (this.options != null) return this.options;

        RegistryEntry<DimensionType> typeEntry = this.resolveType(server);

        if (typeEntry == null)
            throw new IllegalArgumentException("Dimension type is required to create dimension options!");

        return new DimensionOptions(typeEntry, this.generator);
    }

    public interface WorldCreator {
        MultidimServerWorld create(WorldBlueprint blueprint, MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, List<Spawner> spawners, @Nullable RandomSequencesState randomSequencesState, boolean created);
    }
}
