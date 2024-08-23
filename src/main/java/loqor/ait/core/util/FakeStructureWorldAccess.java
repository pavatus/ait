package loqor.ait.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.QueryableTickScheduler;

import loqor.ait.core.AITBlocks;

public class FakeStructureWorldAccess implements StructureWorldAccess {

    private final ServerWorld world;
    private final Map<BlockPos, BlockState> positions = new HashMap<>();

    public FakeStructureWorldAccess(ServerWorld world) {
        this.world = world;
    }

    public Map<BlockPos, BlockState> getPositions() {
        return positions;
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public ServerWorld toServerWorld() {
        return world;
    }

    @Override
    public long getTickOrder() {
        return world.getTickOrder();
    }

    @Override
    public QueryableTickScheduler<Block> getBlockTickScheduler() {
        return world.getBlockTickScheduler();
    }

    @Override
    public QueryableTickScheduler<Fluid> getFluidTickScheduler() {
        return world.getFluidTickScheduler();
    }

    @Override
    public WorldProperties getLevelProperties() {
        return world.getLevelProperties();
    }

    @Override
    public LocalDifficulty getLocalDifficulty(BlockPos pos) {
        return world.getLocalDifficulty(pos);
    }

    @Nullable @Override
    public MinecraftServer getServer() {
        return world.getServer();
    }

    @Override
    public ChunkManager getChunkManager() {
        return world.getChunkManager();
    }

    @Override
    public Random getRandom() {
        return world.getRandom();
    }

    @Override
    public void playSound(@Nullable PlayerEntity except, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) { }

    @Override
    public void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) { }

    @Override
    public void syncWorldEvent(@Nullable PlayerEntity player, int eventId, BlockPos pos, int data) { }

    @Override
    public void emitGameEvent(GameEvent event, Vec3d emitterPos, GameEvent.Emitter emitter) { }

    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        return world.getBrightness(direction, shaded);
    }

    @Override
    public LightingProvider getLightingProvider() {
        return world.getLightingProvider();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return world.getWorldBorder();
    }

    @Nullable @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return world.getBlockEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        BlockState result = this.positions.get(pos);

        if (result != null)
            return result;

        result = world.getBlockState(pos);

        if (result.isOf(AITBlocks.EXTERIOR_BLOCK))
            result = Blocks.AIR.getDefaultState();

        return result;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return world.getFluidState(pos);
    }

    @Override
    public List<Entity> getOtherEntities(@Nullable Entity except, Box box, Predicate<? super Entity> predicate) {
        return List.of();
    }

    @Override
    public <T extends Entity> List<T> getEntitiesByType(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate) {
        return List.of();
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return List.of();
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        this.positions.put(pos.toImmutable(), state);
        return true;
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean move) {
        return true;
    }

    @Override
    public boolean breakBlock(BlockPos pos, boolean drop, @Nullable Entity breakingEntity, int maxUpdateDepth) {
        return true;
    }

    @Override
    public boolean testBlockState(BlockPos pos, Predicate<BlockState> state) {
        return state.test(this.getBlockState(pos));
    }

    @Override
    public boolean testFluidState(BlockPos pos, Predicate<FluidState> state) {
        return state.test(this.getFluidState(pos));
    }

    @Nullable @Override
    public Chunk getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create) {
        return world.getChunk(chunkX, chunkZ, leastStatus, create);
    }

    @Override
    public int getTopY(Heightmap.Type heightmap, int x, int z) {
        return world.getTopY(heightmap, x, z);
    }

    @Override
    public int getAmbientDarkness() {
        return world.getAmbientDarkness();
    }

    @Override
    public BiomeAccess getBiomeAccess() {
        return world.getBiomeAccess();
    }

    @Override
    public RegistryEntry<Biome> getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
        return world.getGeneratorStoredBiome(biomeX, biomeY, biomeZ);
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public DimensionType getDimension() {
        return world.getDimension();
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return world.getRegistryManager();
    }

    @Override
    public FeatureSet getEnabledFeatures() {
        return world.getEnabledFeatures();
    }
}
