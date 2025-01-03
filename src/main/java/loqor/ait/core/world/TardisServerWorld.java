package loqor.ait.core.world;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

import dev.pavatus.multidim.Multidim;
import dev.pavatus.multidim.api.MultidimServerWorld;
import dev.pavatus.multidim.api.VoidChunkGenerator;
import dev.pavatus.multidim.api.WorldBlueprint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;

import loqor.ait.AITMod;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.core.util.WorldUtil;


public class TardisServerWorld extends MultidimServerWorld {

    private static final RegistryKey<Biome> TARDIS_BIOME = RegistryKey.of(RegistryKeys.BIOME, new Identifier(AITMod.MOD_ID, "tardis"));

    private static final WorldBlueprint BLUEPRINT;

    static {
        ServerWorld overworld = WorldUtil.getOverworld();

        BLUEPRINT = new WorldBlueprint(new Identifier(AITMod.MOD_ID, "tardis"))
                .setPersistent(true).shouldTickTime(false).withCreator(TardisServerWorld::new)
                .withType(new Identifier(AITMod.MOD_ID, "tardis_dimension_type")).withSeed(overworld.getSeed())
                .withGenerator(new VoidChunkGenerator(overworld.getRegistryManager().get(RegistryKeys.BIOME), TARDIS_BIOME));
    }

    private ServerTardis tardis;

    public TardisServerWorld(WorldBlueprint blueprint, MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, List<Spawner> spawners, @Nullable RandomSequencesState randomSequencesState, boolean created) {
        super(blueprint, server, workerExecutor, session, properties, worldKey, dimensionOptions, worldGenerationProgressListener, spawners, randomSequencesState, created);
    }

    public void setTardis(ServerTardis tardis) {
        this.tardis = tardis;
    }

    public ServerTardis getTardis() {
        if (this.tardis == null)
            this.tardis = ServerTardisManager.getInstance().demandTardis(this.getServer(),
                    UUID.fromString(this.getRegistryKey().getValue().toString()));

        return tardis;
    }

    private static ServerWorld create(ServerTardis tardis) {
        AITMod.LOGGER.info("Creating Tardis Dimension for Tardis {}", tardis.getUuid());
        TardisServerWorld created = (TardisServerWorld) Multidim.get(ServerLifecycleHooks.get())
                .add(BLUEPRINT, new Identifier(AITMod.MOD_ID, tardis.getUuid().toString()));

        created.setTardis(tardis);

        WorldUtil.blacklist(created);
        return created;
    }

    public static ServerWorld getOrCreate(ServerTardis tardis) {
        ServerWorld world = ServerLifecycleHooks.get().getWorld(RegistryKey.of(RegistryKeys.WORLD,
                new Identifier(AITMod.MOD_ID, tardis.getUuid().toString())));

        if (world == null)
            return create(tardis);

        return world;
    }

    public static boolean isTardisDimension(World world) {
        return world.isClient() ? isTardisDimension((ClientWorld) world) : isTardisDimension((ServerWorld) world);
    }

    public static boolean isTardisDimension(ServerWorld world) {
        return world instanceof TardisServerWorld;
    }

    @Environment(EnvType.CLIENT)
    public static boolean isTardisDimension(ClientWorld world) {
        return world.getBiome(MinecraftClient.getInstance().player.getBlockPos()).matchesId(TARDIS_BIOME.getValue());
    }
}
