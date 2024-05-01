package loqor.ait.core.util.vortex.client;

import loqor.ait.AITMod;
import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientVortexDataHelper {
    /*
        Returns true if the client has cached vortex data for `serverAddress`, false if otherwise.
    */
    public static boolean isVortexDataCached(String serverAddress) {
        return getCachedVortexDataPath(serverAddress).toFile().exists();
    }

    /*
        Returns the path to a locally cached vortex data file.
    */
    public static Path getCachedVortexDataPath(String serverAddress) {
        return FabricLoader.getInstance().getGameDir()
                .resolve(VortexDataHelper.VORTEX_DATA_CLIENT_CACHE_PATH)
                .resolve(String.format("%s.ait-data", serverAddress));
    }

    /*
        Returns a VortexData object cached by the client. Must be called
        only by the client.
    */
    public static VortexData getCachedVortexData(String serverAddress) {
        return VortexDataHelper.readVortexData(getCachedVortexDataPath(serverAddress));
    }

    /*
        Caches a received VortexData object to the local client directory,
        which is typically '/home/<user>/.minecraft/.ait/cache/vortex/<server_ip>.dat' on Linux, macOS,
        and BSD, and '%APPDATA%/.minecraft/.ait/cache/vortex/<server_ip>.dat' on Windows. Must
        only be called by the client.
     */
    public static void cacheVortexData(VortexData data, String serverAddress) {
        try (OutputStream cacheFc = Files.newOutputStream(getCachedVortexDataPath(serverAddress))) {
            cacheFc.write(data.serialize());
        } catch (IOException e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Saving vortex data failed: I/O exception: {}", e.getMessage());
            return;
        }
        AITMod.LOGGER.info("ServerVortexDataHelper: Cached vortex data");
    }
}
