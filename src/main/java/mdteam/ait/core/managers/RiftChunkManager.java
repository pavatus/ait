package mdteam.ait.core.managers;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldEvents;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RiftChunkManager {

    private static final RiftChunkManager INSTANCE = new RiftChunkManager();

    public RiftChunkManager() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> loadRiftChunkData());
        ServerLifecycleEvents.SERVER_STOPPING.register(((server) -> {
            saveRiftChunkData();
            riftChunkArtronLevels = new HashMap<>();
        }));
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
            JsonObject riftChunkData = new JsonObject();
            JsonObject riftChunkArtronLevelsJsonObject = new JsonObject();
            for (Long lll : riftChunkArtronLevels.keySet()) {
                int artron_level = riftChunkArtronLevels.get(lll);
                riftChunkArtronLevelsJsonObject.addProperty(lll.toString(), artron_level);
            }
            riftChunkData.add("rift_chunk_artron_levels", riftChunkArtronLevelsJsonObject);
            FileWriter riftChunkDataFile = new FileWriter(save_path + "riftChunkData.rift");
            //System.out.println(riftChunkData.toString());
            riftChunkDataFile.write(riftChunkData.toString());
            riftChunkDataFile.close();

        } catch (Exception e) {
            // ignore cuz I'm baller like that
        }


    }

    public static void loadRiftChunkData() {
        MinecraftServer server = TardisUtil.getServer();
        String save_path = server.getSavePath(WorldSavePath.ROOT) + "ait/";
        JsonParser jsonParser = new JsonParser();
        try {
            FileReader fileReader = new FileReader(save_path + "riftChunkData.rift");
            Object object = jsonParser.parse(fileReader);
            JsonObject riftChunkData = (JsonObject)object;
            JsonObject riftChunkArtronLevelsJsonObject = (JsonObject) riftChunkData.get("rift_chunk_artron_levels");
            Map<String, JsonElement> map = riftChunkArtronLevelsJsonObject.asMap();
            for (String string : map.keySet()) {
                Long lll = Long.parseLong(string);
                JsonElement jsonElement = map.get(string);
                Integer artron = jsonElement.getAsInt();
                riftChunkArtronLevels.put(lll, artron);
            }
        } catch (Exception e) {
            // ignore cuz I'm baller like that
        }
    }
}
