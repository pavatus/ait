package loqor.ait.core.util.vortex.client;

import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.util.Objects;

public class ClientVortexDataHandler {
    /*
        Registers a JOIN client callback to request VortexData if not present.
     */
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (handler.getServerInfo() == null)
                return;

            if (ClientVortexDataHelper.isVortexDataCached(handler.getServerInfo().address))
                return;
            ClientPlayNetworking.send(VortexDataHelper.REQUEST_SYNC_PACKET, PacketByteBufs.create());
        }));
    }

    /*
        Registers a receiver callback for SYNC_PACKET received from the server.
     */
    public static void subscribe() {
        ClientPlayNetworking.registerGlobalReceiver(VortexDataHelper.SYNC_PACKET,
                ((client, handler, buf, responseSender) -> {
                    ClientVortexDataHelper.cacheVortexData(
                            VortexData.deserialize(buf), Objects.requireNonNull(handler.getServerInfo()).address);
                }));
    }

    /*
        Caches VortexData that was received from the server.
     */
    private static void readServerData(PacketByteBuf buffer, String serverAddress) {
        VortexData data = VortexData.deserialize(buffer);
        ClientVortexDataHelper.cacheVortexData(data, serverAddress);
    }
}
