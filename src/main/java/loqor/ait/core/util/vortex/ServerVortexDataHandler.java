package loqor.ait.core.util.vortex;


import loqor.ait.AITMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class ServerVortexDataHandler {
    /*
        Initialises the server vortex data exchange with the clients by
        registering the callbacks.
     */
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (ServerVortexDataHelper.getClientCachedVortexDataPath(handler.getServerInfo().address).toFile().exists())
                return;
            ClientPlayNetworking.send(ServerVortexDataHelper.REQUEST_SYNC_PACKET, PacketByteBufs.create());
        }));
    }

    /*
        Registers a callback for receiving a VortexData object request from client. Must only
        be called by the server.
     */
    public static void subscribeServer() {
        ServerPlayNetworking.registerGlobalReceiver(ServerVortexDataHelper.REQUEST_SYNC_PACKET,
                (((server, player, handler, buf, responseSender) -> {
                    ServerVortexDataHandler.sendSyncPacketTo(player);
                })));
    }

    /*
        Registers a callback for receiving a VortexData object from server. Must only be called
        by the client.
     */
    public static void subscribeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ServerVortexDataHelper.SYNC_PACKET,
                ((client, handler, buf, responseSender) -> {
                    ServerVortexDataHandler.readServerData(buf, Objects.requireNonNull(handler.getServerInfo()).address);
                }));
    }

    /*
        Sends SYNC_PACKET to the client. Must be registered as a callback for
        ServerPlayConnectionEvents.JOIN. Caller shall be the server, or else
        the call to this function becomes obsolete.
     */
    private static void sendSyncPacketTo(PlayerEntity player) {
        if (player.getWorld().isClient()) {
            AITMod.LOGGER.error("Obsolete sendSyncPacketTo call from a client");
            return;
        }
        VortexData vortexData = ServerVortexDataHelper.getServerVortexData(player.getServer());
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        assert vortexData != null;

        packetByteBuf.writeBytes(vortexData.serialize());
        ServerPlayNetworking.send((ServerPlayerEntity) player, ServerVortexDataHelper.SYNC_PACKET, packetByteBuf);

        AITMod.LOGGER.info("SEND {} -> {} DATA_SIZE {}",
                ServerVortexDataHelper.SYNC_PACKET,
                ((ServerPlayerEntity) player).getIp(),
                vortexData.byteSize()
        );
    }

    private static void readServerData(PacketByteBuf buffer, String serverAddress) {
        VortexData data = VortexData.deserialize(buffer);
        ServerVortexDataHelper.cacheClientVortexData(data, serverAddress);
    }
}
