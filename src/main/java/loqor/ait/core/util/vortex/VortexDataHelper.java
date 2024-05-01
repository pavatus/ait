package loqor.ait.core.util.vortex;

import loqor.ait.AITMod;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
        File dataFd = path.toFile();
        VortexData data = null;

        try {
            FileChannel dataFc = new FileInputStream(dataFd).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)dataFd.length());

            dataFc.read(buffer);
            dataFc.close();

            byte[] decompData = decompressVortexData(buffer.array());

            data = VortexData.deserialize(decompData);
        } catch (IllegalStateException ignored) {
        } catch (IOException e) {
            AITMod.LOGGER.error("VortexDataHelper: Vortex data read failure due to I/O exception: {}", e.getMessage());
            return null;
        }
        return data;
    }

    /*
        Stores VortexData object to a file.
     */
    public static void storeVortexData(Path path, VortexData data) {
        File fd = path.toFile();
        try (FileChannel fc = new FileOutputStream(fd).getChannel()) {
            byte[] sData = data.serialize();
            byte[] compressed = compressVortexData(sData);

            assert compressed != null;
            int bytes = fc.write(ByteBuffer.wrap(compressed));
            fc.close();

            AITMod.LOGGER.info("VortexDataHelper: Stored {} bytes of vortex data", bytes);
        } catch (IOException e) {
            AITMod.LOGGER.error("VortexDataHelper: Storage failed, no such file or directory: {}", e.getMessage());
        }
    }

    public static byte[] compressVortexData(byte[] data) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOs = new GZIPOutputStream(os);
            gzipOs.write(data, 0, data.length);
            gzipOs.close();

            return os.toByteArray();
        } catch (IOException e) {
            AITMod.LOGGER.warn("VortexDataHelper: GZIP initialization failed: {}", e.getMessage());
            return null;
        }
    }

    public static byte[] decompressVortexData(byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);

        try {
            GZIPInputStream gzipIs = new GZIPInputStream(is);
            byte[] decomp = gzipIs.readAllBytes();
            gzipIs.close();
            return decomp;
        } catch (IOException e) {
            AITMod.LOGGER.warn("VortexDataHelper: GZIP initialization failed: {}", e.getMessage());
            return null;
        }
    }
}
