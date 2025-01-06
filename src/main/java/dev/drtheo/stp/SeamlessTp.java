package dev.drtheo.stp;

import dev.drtheo.stp.mixin.EntityInvoker;
import dev.drtheo.stp.mixin.ServerPlayerEntityInvoker;
import dev.drtheo.stp.mixin.ThreadedAnvilChunkStorageAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.WorldChunk;

public class SeamlessTp implements ModInitializer {

    public static final Identifier PRELOAD = new Identifier("stp", "preload");
    public static final Identifier UNLOAD = new Identifier("stp", "unload");
    public static final Identifier TP = new Identifier("stp", "tp");

    public static void preload(ServerPlayerEntity player, ServerWorld world, ChunkPos pos) {
        WorldChunk chunk = world.getChunk(pos.x, pos.z);
        ServerLightingProvider provider = ((ThreadedAnvilChunkStorageAccessor) world.getChunkManager()
                .threadedAnvilChunkStorage).getLightingProvider();

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(world.getRegistryKey().getValue());
        new ChunkDataS2CPacket(chunk, provider, null, null).write(buf);

        ServerPlayNetworking.send(player, PRELOAD, buf);
    }

    public static void unload(ServerPlayerEntity player, ServerWorld world) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(world.getRegistryKey().getValue());

        ServerPlayNetworking.send(player, UNLOAD, buf);
    }

    public static void moveToWorld(ServerPlayerEntity player, ServerWorld destination) {
        ((ServerPlayerEntityInvoker) player).setInTeleportationState(true);

        ServerWorld serverWorld = player.getServerWorld();
        WorldProperties worldProperties = destination.getLevelProperties();

        SeamlessTp.sendTpPacket(player, destination);
        player.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));

        PlayerManager playerManager = player.getServer().getPlayerManager();
        playerManager.sendCommandTree(player);

        serverWorld.removePlayer(player, Entity.RemovalReason.CHANGED_DIMENSION);
        ((EntityInvoker) player).stp$unsetRemoved();

        TeleportTarget teleportTarget = ((ServerPlayerEntityInvoker) player).stp$getTeleportTarget(destination);

        if (teleportTarget != null) {
            player.setServerWorld(destination);
            player.networkHandler.requestTeleport(teleportTarget.position.x, teleportTarget.position.y, teleportTarget.position.z, teleportTarget.yaw, teleportTarget.pitch);
            player.networkHandler.syncWithPlayerPosition();

            destination.onPlayerChangeDimension(player);

            ((ServerPlayerEntityInvoker) player).stp$worldChanged(serverWorld);
            player.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));

            playerManager.sendWorldInfo(player, destination);
            playerManager.sendPlayerStatus(player);

            for (StatusEffectInstance statusEffectInstance : player.getStatusEffects()) {
                player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), statusEffectInstance));
            }

            player.networkHandler.sendPacket(new WorldEventS2CPacket(WorldEvents.TRAVEL_THROUGH_PORTAL, BlockPos.ORIGIN, 0, false));

            ((ServerPlayerEntityInvoker) player).setSyncedExperience(-1);
            ((ServerPlayerEntityInvoker) player).setSyncedHealth(-1.0f);
            ((ServerPlayerEntityInvoker) player).setSyncedFoodLevel(-1);
        }
    }

    /* (server)
    teleport -> player respawn packet
            |-> send world info -> player spawn position packet

       (client)
    player respawn packet -> player & world stuff -> set loading screen
    player spawn position packet -> remove loading screen
     */
    public static void teleport(ServerPlayerEntity player, ServerWorld targetWorld, Vec3d pos, float yaw, float pitch) {
        if (player.getWorld() == targetWorld) {
            player.teleport(targetWorld, pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
            return;
        }

        player.setCameraEntity(player);
        player.stopRiding();

        ServerWorld serverWorld = player.getServerWorld();
        WorldProperties worldProperties = targetWorld.getLevelProperties();

        SeamlessTp.sendTpPacket(player, targetWorld);
        player.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));

        player.server.getPlayerManager().sendCommandTree(player);

        serverWorld.removePlayer(player, Entity.RemovalReason.CHANGED_DIMENSION);
        ((EntityInvoker) player).stp$unsetRemoved();

        player.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
        player.setServerWorld(targetWorld);

        targetWorld.onPlayerTeleport(player);
        ((ServerPlayerEntityInvoker) player).stp$worldChanged(serverWorld);

        player.networkHandler.requestTeleport(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
        player.server.getPlayerManager().sendWorldInfo(player, targetWorld);
        player.server.getPlayerManager().sendPlayerStatus(player);
    }

    @Override
    public void onInitialize() {

    }

    private static void sendTpPacket(ServerPlayerEntity player, ServerWorld targetWorld) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeRegistryKey(targetWorld.getDimensionKey());
        buf.writeRegistryKey(targetWorld.getRegistryKey());
        buf.writeLong(BiomeAccess.hashSeed(targetWorld.getSeed()));
        buf.writeByte(player.interactionManager.getGameMode().getId());
        buf.writeByte(GameMode.getId(player.interactionManager.getPreviousGameMode()));
        buf.writeBoolean(targetWorld.isDebugWorld());
        buf.writeBoolean(targetWorld.isFlat());
        buf.writeByte(3);
        buf.writeOptional(player.getLastDeathPos(), PacketByteBuf::writeGlobalPos);
        buf.writeVarInt(player.getPortalCooldown());

        ServerPlayNetworking.send(player, TP, buf);
    }
}
