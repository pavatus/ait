package dev.pavatus.multidim.api;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import dev.pavatus.multidim.impl.AbstractWorldGenListener;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;

public class WorldBuilder {

    private final Identifier id;

    private long seed;
    private boolean tickTime = true;

    private Identifier typeId;
    private DimensionType type;

    private ChunkGenerator generator;

    public WorldBuilder(Identifier id) {
        this.id = id;
    }

    public WorldBuilder withSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public WorldBuilder tickTime() {
        this.tickTime = true;
        return this;
    }

    public WorldBuilder dontTickTime() {
        this.tickTime = false;
        return this;
    }

    public WorldBuilder withType(Identifier id) {
        this.typeId = id;
        return this;
    }

    public WorldBuilder withType(DimensionType type) {
        return this.withType(null, type);
    }

    public WorldBuilder withType(Identifier id, DimensionType type) {
        this.typeId = id;
        this.type = type;
        return this;
    }

    public WorldBuilder withGenerator(ChunkGenerator generator) {
        this.generator = generator;
        return this;
    }

    public Identifier id() {
        return id;
    }

    public ServerWorld build(MinecraftServer server, DimensionOptions options) {
        SaveProperties saveProps = server.getSaveProperties();
        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, this.id);

        return new ServerWorld(
                server, Util.getMainWorkerExecutor(), ((MultiDimServer) server).multidim$getSession(),
                new UnmodifiableLevelProperties(saveProps, saveProps.getMainWorldProperties()), worldKey,
                options, new AbstractWorldGenListener(), false, BiomeAccess.hashSeed(this.seed),
                ImmutableList.of(), this.tickTime, null
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

    public DimensionOptions buildOptions(MinecraftServer server) {
        RegistryEntry<DimensionType> typeEntry = this.resolveType(server);

        if (typeEntry == null)
            throw new IllegalArgumentException("Dimension type is required to create dimension options!");

        return new DimensionOptions(typeEntry, this.generator);
    }
}
