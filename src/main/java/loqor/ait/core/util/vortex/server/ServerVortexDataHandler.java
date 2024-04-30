package loqor.ait.core.util.vortex.server;


import loqor.ait.AITMod;
import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerVortexDataHandler {
    /*
        Registers a callback for receiving a VortexData object request from client. Must only
        be called by the server.
     */
    public static void subscribe() {
        ServerVortexDataHelper.subscribeDataGenerator();
        ServerPlayNetworking.registerGlobalReceiver(VortexDataHelper.REQUEST_SYNC_PACKET,
                (((server, player, handler, buf, responseSender) -> {
                    ServerVortexDataHandler.sendSyncPacketTo(player);
                })));
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
        VortexData vortexData = ServerVortexDataHelper.getVortexData(player.getServer());
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        assert vortexData != null;

        packetByteBuf.writeBytes(vortexData.serialize());
        ServerPlayNetworking.send((ServerPlayerEntity) player, VortexDataHelper.SYNC_PACKET, packetByteBuf);

        AITMod.LOGGER.info("SEND {} -> {} DATA_SIZE {}",
                VortexDataHelper.SYNC_PACKET,
                ((ServerPlayerEntity) player).getIp(),
                vortexData.byteSize()
        );
    }
}
