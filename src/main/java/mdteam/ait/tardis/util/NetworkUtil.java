package mdteam.ait.tardis.util;

import mdteam.ait.tardis.Tardis;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Collection;

public class NetworkUtil {
    // THESE SHOULD ONLY BE RAN ON SERVER
    public static void send(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        if (player == null) return;
        ServerPlayNetworking.send(player, id, buf);
    }

    /**
     * This method syncs to the players in the tardis' interior and in the tardis' exterior
     * Using this method is going to cause issues if the player is trying to access Tardis data when they dont meet either of these conditions
     * eg. a tooltip on the RemoteItem showing flight progression.
     * TODO - find an alternative. maybe subscribers was better, idk, but this feels safer to me.
     */
    public static void sendToTardisPlayers(Tardis tardis, Identifier id, PacketByteBuf buf) {
        sendToInterior(tardis, id, buf);
        sendToExterior(tardis, id, buf);
    }

    /**
     * @see #sendToTardisPlayers(Tardis, Identifier, PacketByteBuf)
     */
    public static Collection<ServerPlayerEntity> getNearbyTardisPlayers(Tardis tardis) {
        Collection<ServerPlayerEntity> found = getPlayersInInterior(tardis);
        found.addAll(getPlayersNearExterior(tardis));
        return found;
    }
    public static void sendToInterior(Tardis tardis, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
            send(player, id, buf);
        }
    }
    public static Collection<ServerPlayerEntity> getPlayersInInterior(Tardis tardis) {
        return TardisUtil.getPlayersInInterior(tardis);
    }
    // sends to everyone near the exterior
    public static void sendToExterior(Tardis tardis, Identifier id, PacketByteBuf buf) {
        sendToTracking(tardis.getTravel().getPosition(), id, buf);
    }
    public static Collection<ServerPlayerEntity> getPlayersNearExterior(Tardis tardis) {
        return getTracking(tardis.getTravel().getPosition());
    }
    public static void sendToTracking(AbsoluteBlockPos target, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) target.getWorld(), target)) {
            send(player, id, buf);
        }
    }
    public static Collection<ServerPlayerEntity> getTracking(AbsoluteBlockPos target) {
        return PlayerLookup.tracking((ServerWorld) target.getWorld(), target);
    }
    public static void sendToTracking(BlockEntity target, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(target)) {
            send(player, id, buf);
        }
    }
    public static void sendToTracking(Entity target, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(target)) {
            send(player, id, buf);
        }
    }
    public static void sendToWorld(ServerWorld target, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.world(target)) {
            send(player, id, buf);
        }
    }
    public static void sendToNearby(ServerWorld world, Vec3d target, double radius, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.around(world, target, radius)) {
            send(player, id, buf);
        }
    }
    public static void sendToNearby(ServerWorld world, Vec3i target, double radius, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.around(world, target, radius)) {
            send(player, id, buf);
        }
    }
}
