package loqor.ait.core.util.vortex.client;

import loqor.ait.AITMod;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientVortexDataHandler {

    private static final Map<String, VortexData> CACHE = new HashMap<>();

    /*
        Returns the path to a locally cached vortex data file.
    */
    public static Path getCachedVortexDataPath(String name) {
        return FabricLoader.getInstance().getGameDir()
                .resolve(VortexDataHelper.VORTEX_DATA_CLIENT_CACHE_PATH)
                .resolve(String.format("%s.ait-data", name));
    }

    /*
        Returns true if the client has cached vortex data for `serverAddress`, false if otherwise.
    */
    public static boolean isVortexDataCached(String serverAddress) {
        return getCachedVortexDataPath(serverAddress).toFile().exists(); // TODO cache the file in a field
    }

    /*
        Registers a JOIN client callback to request VortexData if not present.
     */
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (isVortexDataCached(WorldUtil.getName(client)))
                return;

            ClientPlayNetworking.send(VortexDataHelper.REQUEST_SYNC_PACKET, PacketByteBufs.create());
        }));

        /*
        Registers a receiver callback for SYNC_PACKET received from the server.
        */
        ClientPlayNetworking.registerGlobalReceiver(VortexDataHelper.SYNC_PACKET,
                ((client, handler, buf, responseSender) -> writeVortexDataCache(
                        VortexData.deserialize(buf.array()), WorldUtil.getName(client)))
        );
    }

    public static VortexData getCachedVortexData(String name) {
        return CACHE.computeIfAbsent(name, s -> VortexDataHelper.readVortexData(getCachedVortexDataPath(s)));
    }

    /*
        Caches a received VortexData object to the local client directory,
        which is typically '/home/<user>/.minecraft/.ait/cache/vortex/<server_ip>.dat' on Linux, macOS,
        and BSD, and '%APPDATA%/.minecraft/.ait/cache/vortex/<server_ip>.dat' on Windows. Must
        only be called by the client.
     */
    public static void writeVortexDataCache(VortexData data, String name) {
        try {
            Path path = getCachedVortexDataPath(name);
            Files.createDirectories(path.getParent());

            OutputStream cacheFc = Files.newOutputStream(path);
            cacheFc.write(data.serialize());
            cacheFc.close();
        } catch (IOException e) {
            AITMod.LOGGER.error("ClientVortexDataHelper: Saving vortex data failed: I/O exception: {}", e.getMessage());
            return;
        }

        AITMod.LOGGER.info("ClientVortexDataHelper: Cached vortex data");
    }
}
