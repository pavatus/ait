package dev.amble.ait.core.util;

import java.util.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import dev.amble.ait.core.AITDimensions;


public class TeleportUtils {
    public static void checkPlayerTeleportation(ServerWorld world) {
        List<ServerPlayerEntity> playersToTeleport = new ArrayList<>();
        List<ServerPlayerEntity> playersToSuck = new ArrayList<>();

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!(player.getWorld().getRegistryKey() == AITDimensions.SPACE)) {
                continue;
            }

            Vec3d playerPos = player.getPos();

            Map<Vec3d, PlanetInfo> planetData = Map.of(
                    new Vec3d(0, 0, 0), new PlanetInfo("minecraft:overworld", 1200, 800), // Overworld
                    new Vec3d(8240, 459f, 0), new PlanetInfo("ait:moon", 150, 400), // Moon
                    new Vec3d(-2500, 1400, 10000), new PlanetInfo(AITDimensions.MARS.toString(), 500, 600) // Mars
            );

            for (Map.Entry<Vec3d, PlanetInfo> entry : planetData.entrySet()) {
                Vec3d planetPos = entry.getKey();
                PlanetInfo planetInfo = entry.getValue();
                double planetRadius = planetInfo.getRadius();
                double suctionRadius = planetInfo.getSuctionRadius();

                double distance = planetPos.distanceTo(playerPos);

                if (distance < suctionRadius) {
                    playersToSuck.add(player);
                    applySuction(playersToSuck, planetPos);
                }
                if (distance < planetRadius) {
                    playersToSuck.remove(player);
                    playersToTeleport.add(player);
                    break;
                }
            }
        }

        for (ServerPlayerEntity player : playersToTeleport) {
            teleportPlayer(player);
        }

        for (Entity entity : world.iterateEntities()) {
            hitDimensionThreshold(entity, 600, AITDimensions.MOON, AITDimensions.SPACE);
            hitDimensionThreshold(entity, World.OVERWORLD, 600, AITDimensions.SPACE, 256);
            hitDimensionThreshold(entity, 500, AITDimensions.MARS, AITDimensions.SPACE);
        }
    }

    private static void hitDimensionThreshold(Entity entity, int tpHeight, RegistryKey<World> worldKeyA, RegistryKey<World> worldKeyB) {
        hitDimensionThreshold(entity, worldKeyA, tpHeight, worldKeyB, tpHeight);
    }

    private static void hitDimensionThreshold(Entity entity, RegistryKey<World> worldKeyA, int tpHeightA, RegistryKey<World> worldKeyB, int tpHeightB) {
        if (entity == null) return;
        if (!(entity.getWorld() instanceof ServerWorld entityWorld))
            return;

        MinecraftServer server = entityWorld.getServer();
        int y = entity.getBlockY();

        ServerWorld worldA = server.getWorld(worldKeyA);
        ServerWorld worldB = server.getWorld(worldKeyB);

        if (y >= tpHeightA && entityWorld == worldA) {
            teleportEntity(entity, worldB);
        }/* else if (y >= tpHeightB && entityWorld == worldB) {
            teleportEntity(entity, worldA);
        }*/
    }

    private static void teleportEntity(Entity entity, ServerWorld targetWorld) {
        if (entity instanceof PlayerEntity player) {
            player.teleport(targetWorld, entity.getX(), entity.getY(), entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
            return;
        }
        entity.teleport(targetWorld, entity.getX(), entity.getY(), entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
    }

    private static void applySuction(List<ServerPlayerEntity> player, Vec3d planetPos) {
        player.forEach(entity -> {
            Vec3d motion = planetPos.subtract(entity.getPos()).normalize().multiply(0.25f);
            entity.setVelocity(entity.getVelocity().add(motion));
            entity.velocityDirty = true;
            entity.velocityModified = true;
        });
    }

    private static void teleportPlayer(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            String dimension = getDimensionForEntity(player);
            if (dimension == null) return;

            String[] parts = dimension.split(":");
            if (parts.length != 2) {
                System.err.println(dimension + "Invalid dimension format! Use 'namespace:dimension' or AITDimensions.<dimension>.getString()");
                return;
            }

            String namespace = parts[0];
            String dimName = parts[1];

            RegistryKey<World> targetWorldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(namespace, dimName));
            ServerWorld targetWorld = server.getWorld(targetWorldKey);

            if (targetWorld != null) {
                BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());
                player.teleport(targetWorld, player.getX(), targetWorld.getChunk(pos).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, (int) player.getX(), (int) player.getZ()), player.getZ(), Set.of(), player.getYaw(), player.getPitch());
            } else {
                System.err.println("Dimension " + dimension + " not found!");
            }
        }
    }

    private static void teleportVehicle(LivingEntity entity) {
        MinecraftServer server = entity.getServer();
        if (server != null) {
            String dimension = getDimensionForEntity(entity);
            if (dimension == null) return;

            String[] parts = dimension.split(":");
            if (parts.length != 2) {
                System.err.println("Invalid dimension format! Use 'namespace:dimension' or AITDimensions.<dimension>.getString()");
                return;
            }

            String namespace = parts[0];
            String dimName = parts[1];

            RegistryKey<World> targetWorldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(namespace, dimName));
            ServerWorld targetWorld = server.getWorld(targetWorldKey);

            if (targetWorld != null) {
                BlockPos pos = new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ());
                entity.teleport(targetWorld, entity.getX(), targetWorld.getChunk(pos).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) entity.getX(), (int) entity.getZ()), entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
            } else {
                System.err.println("Dimension " + dimension + " not found!");
            }
        }
    }

    private static String getDimensionForEntity(LivingEntity entity) {
        Vec3d playerPos = entity.getPos();
        Map<Vec3d, PlanetInfo> planetData = Map.of(
                new Vec3d(0, 0, 0), new PlanetInfo("minecraft:overworld", 900, 200),
                new Vec3d(8240, 459, 0), new PlanetInfo("ait:moon", 150, 140),
                new Vec3d(-2500, 300, 0), new PlanetInfo(AITDimensions.MARS.toString(), 500, 150)
        );

        for (Map.Entry<Vec3d, PlanetInfo> entry : planetData.entrySet()) {
            Vec3d planetPos = entry.getKey();
            PlanetInfo planetInfo = entry.getValue();
            String dimension = planetInfo.getDimension();
            double planetRadius = planetInfo.getRadius();

            double distance = planetPos.distanceTo(playerPos);
            if (distance < planetRadius) {
                return dimension;
            }
        }
        return null;
    }

    private static class PlanetInfo {
        private final String dimension;
        private final double radius;
        private final double suctionRadius;

        public PlanetInfo(String dimension, double radius, double suctionRadius) {
            this.dimension = dimension;
            this.radius = radius;
            this.suctionRadius = radius + suctionRadius;
        }

        public String getDimension() {
            return dimension;
        }

        public double getRadius() {
            return radius;
        }

        public double getSuctionRadius() {
            return suctionRadius;
        }
    }
}
