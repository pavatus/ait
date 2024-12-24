package dev.pavatus.multidim;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.apache.commons.io.FilenameUtils;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

public class MultiDimFileManager {
    private final Set<RegistryKey<World>> toWrite = new ReferenceOpenHashSet<>();
    private final Gson gson = new Gson();

    private static Path getRootSavePath(Path root) {
        return root.resolve(".multidim");
    }

    public static Path getRootSavePath(MinecraftServer server) {
        return MultiDimFileManager.getRootSavePath(server.getSavePath(WorldSavePath.ROOT));
    }
    private static Path getSavePath(MinecraftServer server, Identifier id) {
        return getRootSavePath(server).resolve(id.getNamespace()).resolve(id.getPath() + ".json");
    }

    public void add(ServerWorld world) {
        this.add(world.getRegistryKey());
    }
    public void add(RegistryKey<World> world) {
        this.toWrite.add(world);
    }

    public void write(MinecraftServer server) {
        for (RegistryKey<World> key : this.toWrite) {
            this.write(server, key);
        }
        this.toWrite.clear();
    }
    private void write(MinecraftServer server, RegistryKey<World> key) {
        // basically immersive portals code lol
        ServerWorld world = server.getWorld(key);
        if (world == null) return;

        DimensionOptions options = new DimensionOptions(world.getDimensionEntry(), world.getChunkManager().getChunkGenerator());
        File file = getSavePath(server, key.getValue()).toFile();

        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }

            try (FileWriter writer = new FileWriter(file)) {
                RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, server.getRegistryManager());
                JsonElement result = DimensionOptions.CODEC.encode(options, ops, new JsonObject()).get().left().orElse(null);
                if (result == null) {
                    MultiDim.LOGGER.warn("Couldn't encode world file! {}", key.getValue());
                    return;
                }

                this.gson.toJson(result, writer);
            }
        } catch (IOException e) {
            MultiDim.LOGGER.warn("Couldn't create world file! {}", key.getValue(), e);
        }
    }

    public HashMap<RegistryKey<World>, DimensionOptions> read(MinecraftServer server) {
        HashMap<RegistryKey<World>, DimensionOptions> map = new HashMap<>();

        if (server == null || !server.isRunning()) return map;

        Path root = getRootSavePath(server);
        if (!Files.exists(root)) return map;

        File[] files = root.toFile().listFiles();
        if (files == null) return map;

        for (File namespace : files) {
            if (!namespace.isDirectory()) continue;

            for (File file : namespace.listFiles()) {
                Identifier id = new Identifier(namespace.getName(), FilenameUtils.getBaseName(file.getName()));

                Pair<RegistryKey<World>, DimensionOptions> result = this.read(server, id);
                if (result == null) continue;
                map.put(result.getFirst(), result.getSecond());
                this.toWrite.add(result.getFirst());
            }
        }

        return map;
    }
    private Pair<RegistryKey<World>, DimensionOptions> read(MinecraftServer server, Identifier id) {
        File file = getSavePath(server, id).toFile();

        RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, server.getRegistryManager());

        try {
            JsonElement element;

            try(FileReader reader = new FileReader(file)) {
                element = JsonParser.parseReader(reader);
            }

            Optional<Pair<DimensionOptions, JsonElement>> result = DimensionOptions.CODEC.decode(ops, element).get().left();
            if (result.isEmpty()) {
                MultiDim.LOGGER.warn("Couldn't decode world file! {}", id);
                return null;
            }

            DimensionOptions options = result.get().getFirst();
            return Pair.of(RegistryKey.of(RegistryKeys.WORLD, id), options);
        } catch (Throwable e) {
            MultiDim.LOGGER.warn("Couldn't read world file! {}", id, e);
            return null;
        }
    }

    private Gson getGson() {
        return this.gson;
    }
}
