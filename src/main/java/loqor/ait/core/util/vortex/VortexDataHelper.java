package loqor.ait.core.util.vortex;

import loqor.ait.AITMod;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class VortexDataHelper {
    public static final String VORTEX_DATA_SERVER_CACHE_PATH = "vortex/vortex_nodes.ait-data";
    public static final String VORTEX_DATA_CLIENT_CACHE_PATH = ".cache/ait/vortex";
    public static final Identifier SYNC_PACKET = new Identifier(AITMod.MOD_ID, "vortex_sync");
    public static final Identifier REQUEST_SYNC_PACKET = new Identifier(AITMod.MOD_ID, "request_vortex_sync");

    /*
        Returns a VortexData object read from a cached vortex.dat. Can be
        called by either server or client.
     */
    public static VortexData readVortexData(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return VortexData.deserialize(decompressVortexData(in.readAllBytes()));
        } catch (IOException e) {
            AITMod.LOGGER.error("VortexDataHelper: Unable to read vortex data");
        }

        return null;
    }

    /*
        Stores VortexData object to a file.
     */
    public static void storeVortexData(Path path, VortexData data) {
        try {
            Files.createDirectories(path.getParent());

            OutputStream out = Files.newOutputStream(path);
            out.write(compressVortexData(data.serialize()));
            out.close();
        } catch (IOException e) {
            AITMod.LOGGER.error("VortexDataHelper: Storage failed: {}", e.getMessage());
        }
    }

    public static byte[] compressVortexData(byte[] data) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOs = new GZIPOutputStream(os)) {
            gzipOs.write(data, 0, data.length);
        } catch (IOException e) {
            AITMod.LOGGER.warn("VortexDataHelper: GZIP initialization failed: {}", e.getMessage());
            return null;
        }

        return os.toByteArray();
    }

    public static byte[] decompressVortexData(byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try (GZIPInputStream gzipIs = new GZIPInputStream(is)) {
            return gzipIs.readAllBytes();
        } catch (IOException e) {
            AITMod.LOGGER.warn("VortexDataHelper: GZIP initialization failed: {}", e.getMessage());
            return null;
        }
    }
}
