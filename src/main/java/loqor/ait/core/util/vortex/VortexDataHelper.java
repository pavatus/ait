package loqor.ait.core.util.vortex;

import loqor.ait.AITMod;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class VortexDataHelper {
    public static final String VORTEX_DATA_SERVER_CACHE_PATH = "vortex/vortex.dat";
    public static final String VORTEX_DATA_CLIENT_CACHE_PATH = ".cache/ait/vortex";
    public static final Identifier SYNC_PACKET = new Identifier(AITMod.MOD_ID, "vortex_sync");
    public static final Identifier REQUEST_SYNC_PACKET = new Identifier(AITMod.MOD_ID, "request_vortex_sync");

    /*
        Returns a VortexData object read from a cached vortex.dat. Can be
        called by either server or client.
     */
    public static VortexData readVortexData(Path path) {
        File dataFd = path.toFile();
        VortexData data;

        try {
            FileChannel dataFc = new FileInputStream(dataFd).getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect((int)dataFd.length());

            dataFc.read(buffer);
            dataFc.close();

            data = VortexData.deserialize(buffer.array());

            for (VortexNode node : data.nodes()) {
                AITMod.LOGGER.info("POS: {} | PTL: {} | PTR: {} | LEAF: {}", node.pos, node.ptrToLeft, node.ptrToRight, node.isLeaf);
            }
        } catch (Exception e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Vortex data read failure: {}", e.getMessage());
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
            try  {
                fc.write(ByteBuffer.wrap(data.serialize()));
                fc.close();
            } catch (IOException e) {
                AITMod.LOGGER.error("VortexDataHelper: Storage failure due to I/O exception: {}", e.getMessage());
            }
        } catch (IOException e) {
            AITMod.LOGGER.error("VortexDataHelper: Storage failed, no such file or directory: {}", e.getMessage());
        }
    }
}
