package loqor.ait.core.util.vortex.client;

import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class ClientVortexDataHandler {
    /*
        Registers a JOIN client callback to request VortexData if not present.
     */
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (ClientVortexDataHelper.isVortexDataCached(client.isInSingleplayer() ?
                    client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString() :
                    client.getCurrentServerEntry().address)) {
                return;
            }
            ClientPlayNetworking.send(VortexDataHelper.REQUEST_SYNC_PACKET, PacketByteBufs.create());
            try {
                Files.createDirectories(ClientVortexDataHelper.getCachedVortexDataPath(client.isInSingleplayer() ?
                        client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString() :
                        client.getCurrentServerEntry().address).getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    /*
        Registers a receiver callback for SYNC_PACKET received from the server.
     */
    public static void subscribe() {
        ClientPlayNetworking.registerGlobalReceiver(VortexDataHelper.SYNC_PACKET,
                ((client, handler, buf, responseSender) -> {
                    ClientVortexDataHelper.cacheVortexData(
                            VortexData.deserialize(buf.array()), client.isInSingleplayer() ?
                                    client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString() :
                                    client.getCurrentServerEntry().address);
                }));
    }

    /*
        Caches VortexData that was received from the server.
     */
    private static void readServerData(PacketByteBuf buffer, String serverAddress) {
        VortexData data = VortexData.deserialize(buffer.array());
        ClientVortexDataHelper.cacheVortexData(data, serverAddress);
    }
}
