package dev.amble.ait.core.util;

import java.util.*;

import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import dev.amble.ait.core.AITDimensions;
import dev.amble.ait.core.entities.FlightTardisEntity;
import dev.amble.ait.core.tardis.Tardis;


public class TeleportUtils {
    public static void checkPlayerTeleportation(ServerWorld world) {
        List<ServerPlayerEntity> playersToTeleport = new ArrayList<>();

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!(player.getWorld().getRegistryKey() == AITDimensions.SPACE)) {
                continue;
            }

            Vec3d playerPos = player.getPos();

            Map<Vec3d, PlanetInfo> planetData = Map.of(
                    new Vec3d(0, 0, 0), new PlanetInfo("minecraft:overworld", 900, 200), // Overworld
                    new Vec3d(2000, 0, 0), new PlanetInfo(AITDimensions.MOON.toString(), 150, 100), // Moon
                    new Vec3d(-2500, 300, 0), new PlanetInfo(AITDimensions.MARS.toString(), 500, 150) // Mars
            );

            for (Map.Entry<Vec3d, PlanetInfo> entry : planetData.entrySet()) {
                Vec3d planetPos = entry.getKey();
                PlanetInfo planetInfo = entry.getValue();
                String dimension = planetInfo.getDimension();
                double planetRadius = planetInfo.getRadius();
                double suctionRadius = planetInfo.getSuctionRadius();

                double distance = planetPos.distanceTo(playerPos);

                if (distance < planetRadius) {
                    playersToTeleport.add(player);
                    break;
                } else if (distance < suctionRadius) {
                    applySuction(player, planetPos);
                }
            }
        }

        playersToTeleport.forEach(player -> {
            if (player.getVehicle() instanceof FlightTardisEntity entity) {
                if (entity.tardis() == null) return;
                Tardis tardis = entity.tardis().get();
                tardis.flight().flying().set(false);
            }
            if (player.getVehicle() instanceof LivingEntity entity) {
                teleportVehicle(entity);
            }
        });

        for (ServerPlayerEntity player : playersToTeleport) {
            teleportPlayer(player);
            playersToTeleport.remove(player);
        }

        for (Entity entity : world.iterateEntities()) {
            hitDimensionThreshold(entity, 600, AITDimensions.MOON, AITDimensions.SPACE);
            hitDimensionThreshold(entity, World.OVERWORLD, 600, AITDimensions.SPACE, 256);
            hitDimensionThreshold(entity, 500, AITDimensions.MARS, AITDimensions.SPACE);
        }
    }

    @Unique private static void hitDimensionThreshold(Entity entity, int tpHeight, RegistryKey<World> worldKeyA, RegistryKey<World> worldKeyB) {
        hitDimensionThreshold(entity, worldKeyA, tpHeight, worldKeyB, tpHeight);
    }

    @Unique private static void hitDimensionThreshold(Entity entity, RegistryKey<World> worldKeyA, int tpHeightA, RegistryKey<World> worldKeyB, int tpHeightB) {
        if (!(entity.getWorld() instanceof ServerWorld entityWorld))
            return;

        MinecraftServer server = entityWorld.getServer();
        int y = entity.getBlockY();

        ServerWorld worldA = server.getWorld(worldKeyA);
        ServerWorld worldB = server.getWorld(worldKeyB);

        if (entity.hasVehicle()) {
            if (entity instanceof PlayerEntity player) {
                Entity tardisEntity = player.getVehicle();
                if (tardisEntity instanceof FlightTardisEntity flightTardis) {
                    Tardis tardis = flightTardis.tardis().get();
                    if (flightTardis.tardis() == null) return;
                    if (y >= tpHeightA && entityWorld == worldA) {
                        moveToWorldWithPassenger(flightTardis, tardis, player, worldB);
                    }/* else if (y >= tpHeightB && entityWorld == worldB) {
                        moveToWorldWithPassenger(flightTardis, tardis, player, worldA);
                    }*/
                    return;
                }
            }
        }

        if (y >= tpHeightA && entityWorld == worldA) {
            entity.moveToWorld(worldB);
        }/* else if (y >= tpHeightB && entityWorld == worldB) {
            entity.moveToWorld(worldA);
        }*/
    }

    @Unique private static void moveToWorldWithPassenger(FlightTardisEntity tardisEntity, Tardis tardis, PlayerEntity player, ServerWorld b) {
        player.stopRiding();
        tardis.flight().flying().set(false);
        player.moveToWorld(b);
        tardisEntity.moveToWorld(b);
        tardis.flight().flying().set(true);
        player.startRiding(tardisEntity);
    }

    private static void applySuction(ServerPlayerEntity player, Vec3d planetPos) {
        Vec3d playerPos = player.getPos();

        // Atmosphere succ goes here
    }

    private static void teleportPlayer(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            String dimension = getDimensionForEntity(player);
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
                player.teleport(targetWorld, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
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
                entity.teleport(targetWorld, entity.getX(), entity.getY(), entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
            } else {
                System.err.println("Dimension " + dimension + " not found!");
            }
        }
    }

    private static String getDimensionForEntity(LivingEntity entity) {
        Vec3d playerPos = entity.getPos();
        Map<Vec3d, PlanetInfo> planetData = Map.of(
                new Vec3d(0, 0, 0), new PlanetInfo("minecraft:overworld", 900, 200),
                new Vec3d(8240, 459, 0), new PlanetInfo(AITDimensions.MOON.toString(), 150, 140),
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
            this.suctionRadius = suctionRadius;
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
