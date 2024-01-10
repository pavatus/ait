package mdteam.ait.core;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

// todo move all packets / identifiers to here for easier reading imo
// todo instead of sending updates to everyone, only send updates to "tracking" !! ( especially for animation stuff )
public class AITMessages {
    public static final Identifier CANCEL_DEMAT_SOUND = new Identifier(AITMod.MOD_ID, "cancel_demat_sound");

    // THESE SHOULD ONLY BE RAN ON SERVER
    public static void send(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        if (player == null) return;
        ServerPlayNetworking.send(player, id, buf);
    }
    public static void sendToAll(Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.all(TardisUtil.getServer())) {
            send(player, id, buf);
        }
    }
    public static void sendToInterior(Tardis tardis, Identifier id, PacketByteBuf buf) {
        for (PlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
            send((ServerPlayerEntity) player, id, buf);
        }
    }
    // sends to everyone near the exterior
    public static void sendToExterior(Tardis tardis, Identifier id, PacketByteBuf buf) {
        sendToTracking(tardis.getTravel().getPosition(), id, buf);
    }
    public static void sendToTracking(AbsoluteBlockPos target, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) target.getWorld(), target)) {
            send(player, id, buf);
        }
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
