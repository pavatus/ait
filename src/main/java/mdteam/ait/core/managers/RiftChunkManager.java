package mdteam.ait.core.managers;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.ChunkPos;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RiftChunkManager {

    private static final RiftChunkManager INSTANCE = new RiftChunkManager();

    public RiftChunkManager() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> loadRiftChunkData());
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> saveRiftChunkData());
    }

    public static void init() {
        // nothing
    }
    private static Map<Long, Integer> riftChunkArtronLevels = new HashMap<>();

    public static void setArtronLevels(ChunkPos chunkPos, Integer artronLevels) {
        if (!riftChunkArtronLevels.containsKey(chunkPos.toLong())) {
            riftChunkArtronLevels.put(chunkPos.toLong(), artronLevels);
        } else {
            riftChunkArtronLevels.replace(chunkPos.toLong(), artronLevels);
        }
    }

    public static Integer getArtronLevels(ChunkPos chunkPos) {
        if (!riftChunkArtronLevels.containsKey(chunkPos.toLong())) {
            AITMod.LOGGER.warn("We shouldn't be getting this here, we did something wrong, but don't worry I have a backup for this issue, we re-init the RiftChunk");
            initRiftChunk(chunkPos);
            return riftChunkArtronLevels.get(chunkPos.toLong());
        } else {
            return riftChunkArtronLevels.get(chunkPos.toLong());
        }
    }

    public static void initRiftChunk(ChunkPos chunkPos) {
        if (riftChunkArtronLevels.containsKey(chunkPos.toLong())) return; // Already been initialized
        Random random = new Random();
        riftChunkArtronLevels.put(chunkPos.toLong(), random.nextInt(100, 800));
    }

    public static void saveRiftChunkData() {
        MinecraftServer server = TardisUtil.getServer();
        String save_path = server.getSavePath(WorldSavePath.ROOT) + "ait/";
        try {
            saveRawBytesToFile(convertMapToBytes(riftChunkArtronLevels), save_path + "chunkArtronLevels.bytes");
        } catch (Exception e) {
            // ignore cuz I'm baller like that
        }


    }

    public static void loadRiftChunkData() {
        MinecraftServer server = TardisUtil.getServer();
        String save_path = server.getSavePath(WorldSavePath.ROOT) + "ait/";
        try {
            byte[] artronLevelBytes = loadRawBytesFromFile(save_path + "chunkArtronLevels.bytes");
            riftChunkArtronLevels = convertBytesToMap(artronLevelBytes);
            if (riftChunkArtronLevels == null) {
                riftChunkArtronLevels = new HashMap<>();
            }
        } catch (Exception e) {
            // ignore cuz I'm baller like that
        }
    }

    private static void saveRawBytesToFile(byte[] bytes, String filepath) {
        try {
            OutputStream out = new FileOutputStream(filepath, false);
            Socket sock = new Socket();
            InputStream in = sock.getInputStream();
            int bytes_read;
            while ((bytes_read = in.read(bytes)) != -1) {
                out.write(bytes, 0, bytes_read);
            }
            out.close();
        } catch(Exception e) {
            // ignore
        }
    }

    private static byte[] loadRawBytesFromFile(String filepath) {
        File file = new File(filepath);
        byte[] bytes;
        try {
            bytes = Files.toByteArray(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;

    }

    private static byte[] convertMapToBytes(Map<?, ?> map) throws IOException {
        return SerializationUtils.serialize((Serializable) map);
    }

    private static Map<Long, Integer> convertBytesToMap(byte[] bytes) throws IOException {
        return SerializationUtils.deserialize(bytes);
    }
}
