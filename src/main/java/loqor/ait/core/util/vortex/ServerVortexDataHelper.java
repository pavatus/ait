package loqor.ait.core.util.vortex;

import loqor.ait.AITMod;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class ServerVortexDataHelper {
    public static final String VORTEX_DATA_SERVER_CACHE_PATH = "vortex/vortex.dat";
    public static final String VORTEX_DATA_CLIENT_CACHE_PATH = ".ait/cache/vortex";
    public static final Identifier SYNC_PACKET = new Identifier(AITMod.MOD_ID, "vortex_sync");
    public static final Identifier REQUEST_SYNC_PACKET = new Identifier(AITMod.MOD_ID, "request_vortex_sync");

    /*
        Returns the path to a world save stored vortex data file.
     */
    public static Path getServerVortexDataPath(MinecraftServer server) {
        return ServerTardisManager.getRootSavePath(server).resolve(VORTEX_DATA_SERVER_CACHE_PATH);
    }

    /*
        Returns the path to a locally cached vortex data file.
     */
    public static Path getClientCachedVortexDataPath(String serverAddress) {
        return FabricLoader.getInstance().getGameDir()
                .resolve(VORTEX_DATA_CLIENT_CACHE_PATH)
                .resolve(String.format("%s.dat", serverAddress));
    }

    /*
        Returns a VortexData object cached by the client. Must be called
        only by the client.
     */
    public static VortexData getCachedClientVortexData(String serverAddress) {
        return readVortexData(getClientCachedVortexDataPath(serverAddress));
    }

    /*
        Returns a VortexData object stored in the world save directory. Must
        be only called by the server.
     */
    public static VortexData getServerVortexData(MinecraftServer server) {
        return readVortexData(getServerVortexDataPath(server));
    }

    /*
        Caches a received VortexData object to the local client directory,
        which is typically '/home/<user>/.minecraft/.ait/cache/vortex/<server_ip>.dat' on Linux, macOS,
        and BSD, and '%APPDATA%/.minecraft/.ait/cache/vortex/<server_ip>.dat' on Windows. Must
        only be called by the client.
     */
    public static void cacheClientVortexData(VortexData data, String serverAddress) {
        File cacheFd = getClientCachedVortexDataPath(serverAddress).toFile();
        FileChannel cacheFc = null;
        try {
            cacheFc = new FileOutputStream(cacheFd, false).getChannel();
        } catch (FileNotFoundException e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Caching vortex data failed: Unknown Exception: {}", e.getMessage());
            return;
        }

        AITMod.LOGGER.info("ServerVortexDataHelper: Caching received vortex data, size: {}", data.byteSize());
        try {
            int bytes = cacheFc.write(data.serialize());
        } catch (IOException e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Saving vortex data failed: I/O exception: {}", e.getMessage());
            return;
        }
        AITMod.LOGGER.info("ServerVortexDataHelper: Cached vortex data");
    }

    /*
        Returns a VortexData object read from a cached vortex.dat. Can be
        called by either server or client.
     */
    private static VortexData readVortexData(Path path) {
        File dataFd = path.toFile();
        VortexData data;

        try {
            FileChannel dataFc = new FileInputStream(dataFd).getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect((int)dataFd.length());

            dataFc.read(buffer);

            data = VortexData.deserialize(buffer);
        } catch (Exception e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Vortex data read failure: {}", e.getMessage());
            return null;
        }
        return data;
    }
}
