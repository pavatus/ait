package loqor.ait.core.managers;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonObject;
import loqor.ait.api.tardis.ArtronHolder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.world.chunk.WorldChunk;

@Deprecated(forRemoval = true)
public class RiftChunkManager {

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(RiftChunkManager::load);

        ServerLifecycleEvents.SERVER_STOPPING.register(((server) -> {
            save(server);

            riftChunkData = new HashMap<>();
        }));
    }

    private static Map<RegistryKey<World>, Map<ChunkPos, Integer>> riftChunkData = new HashMap<>();

    public static void setArtronLevels(World world, ChunkPos chunkPos, Integer artronLevels) {
        if (!isRiftChunk(chunkPos))
            return;

        if (!riftChunkData.containsKey(world.getRegistryKey())) {
            HashMap<ChunkPos, Integer> temp = new HashMap<>();
            temp.put(chunkPos, artronLevels);
            riftChunkData.put(world.getRegistryKey(), temp);
            return;
        }

        if (!riftChunkData.get(world.getRegistryKey()).containsKey(chunkPos)) {
            riftChunkData.get(world.getRegistryKey()).put(chunkPos, artronLevels);
            return;
        }

        riftChunkData.get(world.getRegistryKey()).replace(chunkPos, artronLevels);
    }

    public static void setArtronLevels(World world, BlockPos blockPos, Integer artronLevels) {
        setArtronLevels(world, new ChunkPos(blockPos), artronLevels);
    }

    public static Integer getArtronLevels(World world, ChunkPos chunkPos) {
        if (!isRiftChunk(chunkPos))
            return 0;

        if (!riftChunkData.containsKey(world.getRegistryKey())) {
            initRiftChunk(world, chunkPos);
            return getArtronLevels(world, chunkPos);
        }

        if (!riftChunkData.get(world.getRegistryKey()).containsKey(chunkPos)) {
            initRiftChunk(world, chunkPos);
            return getArtronLevels(world, chunkPos);
        }

        return riftChunkData.get(world.getRegistryKey()).get(chunkPos);
    }

    public static Integer getArtronLevels(World world, BlockPos blockPos) {
        return getArtronLevels(world, new ChunkPos(blockPos));
    }

    public static void initRiftChunk(World world, ChunkPos chunkPos) {
        if (riftChunkData.containsKey(world.getRegistryKey())
                && riftChunkData.get(world.getRegistryKey()).containsKey(chunkPos))
            return;

        Random random = new Random();
        if (!riftChunkData.containsKey(world.getRegistryKey())) {
            HashMap<ChunkPos, Integer> temp = new HashMap<>();
            temp.put(chunkPos, random.nextInt(300, 1000));
            riftChunkData.put(world.getRegistryKey(), temp);
            return;
        }

        riftChunkData.get(world.getRegistryKey()).put(chunkPos, random.nextInt(100, 800));
    }

    public static void save(MinecraftServer server) {
        String save_file_path = server.getSavePath(WorldSavePath.ROOT) + "ait_rift_chunk_data.json";

        try {
            JsonObject jsonRiftChunkData = new JsonObject();
            for (RegistryKey<World> key : riftChunkData.keySet()) {
                JsonObject jsonWorldData = new JsonObject();
                for (ChunkPos chunkPos : riftChunkData.get(key).keySet()) {
                    jsonWorldData.addProperty(String.valueOf(chunkPos.toLong()), riftChunkData.get(key).get(chunkPos));
                }
                jsonRiftChunkData.add(key.getValue().toString(), jsonWorldData);
            }
            FileWriter fileWriter = new FileWriter(save_file_path);
            fileWriter.write(jsonRiftChunkData.toString());
            fileWriter.close();
        } catch (Exception e) {
            AITMod.LOGGER.error("Failed to save rift chunk data!", e);
        }
    }

    public static boolean isRiftChunk(ChunkPos chunkPos) {
        return ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z, TardisUtil.getOverworld().getSeed(), 987234910L)
                .nextInt(8) == 0;
    }

    public static boolean isRiftChunk(BlockPos blockPos) {
        return isRiftChunk(new ChunkPos(blockPos));
    }

    public static void load(MinecraftServer server) {
        String save_file_path = server.getSavePath(WorldSavePath.ROOT) + "ait_rift_chunk_data.json";
        try {
            FileReader fileReader = new FileReader(save_file_path);
            JsonObject jsonRiftChunkData = JsonHelper.deserialize(fileReader);
            for (String string : jsonRiftChunkData.keySet()) {
                RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, new Identifier(string));
                JsonObject jsonWorldData = (JsonObject) jsonRiftChunkData.get(string);
                for (String string2 : jsonWorldData.keySet()) {
                    ChunkPos chunkPos = new ChunkPos(Long.parseLong(string2));
                    int artronLevels = jsonWorldData.get(string2).getAsInt();
                    if (!riftChunkData.containsKey(key)) {
                        HashMap<ChunkPos, Integer> temp = new HashMap<>();
                        temp.put(chunkPos, artronLevels);
                        riftChunkData.put(key, temp);
                        continue;
                    }
                    if (!riftChunkData.get(key).containsKey(chunkPos)) {
                        riftChunkData.get(key).put(chunkPos, artronLevels);
                        continue;
                    }
                    riftChunkData.get(key).replace(chunkPos, artronLevels);
                }
            }
        } catch (Exception e) {
            AITMod.LOGGER.error("Failed to load rift chunk data!", e);
        }
    }
}
