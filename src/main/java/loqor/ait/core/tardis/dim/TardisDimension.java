package loqor.ait.core.tardis.dim;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import dev.pavatus.multidim.MultiDim;
import dev.pavatus.multidim.api.VoidChunkGenerator;
import dev.pavatus.multidim.api.WorldBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.compat.immersive.PortalsHandler;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.TardisManager;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.core.util.WorldUtil;


public class TardisDimension {
    private static WorldBuilder builder(ServerTardis tardis) {
        return new WorldBuilder(new Identifier(AITMod.MOD_ID, tardis.getUuid().toString()))
                .loadOnStartup(true)
                .withType(new Identifier(AITMod.MOD_ID, "tardis_dimension_type"))
                .withSeed(WorldUtil.getOverworld().getSeed())
                .withGenerator(new VoidChunkGenerator(WorldUtil.getOverworld().getRegistryManager().get(RegistryKeys.BIOME), RegistryKey.of(RegistryKeys.BIOME, new Identifier(AITMod.MOD_ID, "tardis"))));
    }

    private static ServerWorld create(WorldBuilder builder) {
        if (DependencyChecker.hasPortals()) {
            AITMod.LOGGER.info("Immersive Portals Detected! Using their API instead..");
            return PortalsHandler.addWorld(builder);
        }

        return MultiDim.get(ServerLifecycleHooks.get()).add(builder);
    }
    private static ServerWorld create(ServerTardis tardis) {
        WorldBuilder builder = builder(tardis);

        AITMod.LOGGER.info("Creating Tardis Dimension for Tardis {}", tardis.getUuid());
        ServerWorld created = create(builder);

        WorldUtil.blacklist(created);

        return created;
    }
    public static Optional<ServerWorld> get(ServerTardis tardis) {
        ServerWorld found = ServerLifecycleHooks.get().getWorld(RegistryKey.of((RegistryKey)RegistryKeys.WORLD, new Identifier(AITMod.MOD_ID, tardis.getUuid().toString())));
        return Optional.ofNullable(found);
    }
    public static ServerWorld getOrCreate(ServerTardis tardis) {
        return get(tardis).orElseGet(() -> create(tardis));
    }
    public static Optional<Tardis> get(World world) {
        UUID uuid;

        try {
            uuid = UUID.fromString(world.getRegistryKey().getValue().getPath());
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.ofNullable(TardisManager.with(world, ((o, manager) -> manager.demandTardis(o, uuid))));
    }
    public static void withTardis(ServerWorld world, Consumer<ServerTardis> consumer) {
        UUID uuid;

        try {
            uuid = UUID.fromString(world.getRegistryKey().getValue().getPath());
        } catch (Exception e) {
            consumer.accept(null);
            return;
        }

        ServerTardisManager.getInstance().getTardis(world.getServer(), uuid, consumer);
    }
    public static boolean isTardisDimension(RegistryKey<World> world) {
        Identifier value = world.getValue();

        try {
            UUID.fromString(value.getPath());
        } catch (Exception e) {
            return false;
        }

        return value.getNamespace().equals(AITMod.MOD_ID);
    }
    public static boolean isTardisDimension(World world) {
        return (!world.isClient()) ? isTardisDimension((ServerWorld) world) : isTardisDimension((ClientWorld) world);
    }

    private static boolean isTardisDimension(ServerWorld world) {
        return get(world).isPresent();
    }

    @Environment(EnvType.CLIENT)
    private static boolean isTardisDimension(ClientWorld world) {
        return world.getBiome(MinecraftClient.getInstance().player.getBlockPos()).matchesId(new Identifier(AITMod.MOD_ID, "tardis"));
    }
}
