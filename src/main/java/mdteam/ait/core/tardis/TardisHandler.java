package mdteam.ait.core.tardis;

import mdteam.ait.AITMod;
import net.minecraft.util.WorldSavePath;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.datafixer.fix.BlockEntitySignTextStrictJsonFix.GSON;

public class TardisHandler {

    public static Map<UUID, Tardis> tardisses = new HashMap<>();

    @Nullable
    public static Tardis getTardis(UUID uuid) {
        if (!tardisses.containsKey(uuid)) {
            loadTardis(uuid);
        }
        return tardisses.get(uuid);
    }

    public static void loadTardis(UUID uuid) {
        if(tardisses.containsKey(uuid)) return;
        File file = new File(AITMod.mcServer.getSavePath(WorldSavePath.ROOT) + "ait/" + uuid + ".json");
        if(!file.exists()) return;
        try {
            String fileContent = new String(Files.readAllBytes(file.toPath()));
            tardisses.put(uuid, GSON.fromJson(fileContent, Tardis.class));
        } catch (Exception e) {
            AITMod.LOGGER.warn("Tardis " + uuid + " does not exist");
        }
    }

    public static void saveTardis(Tardis tardis) {
        File file = new File(AITMod.mcServer.getSavePath(WorldSavePath.ROOT) + "ait/" + tardis.getUuid() + ".json");
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath()));
            writer.write(GSON.toJson(tardis));
            writer.close();
        } catch (Exception e) {
            AITMod.LOGGER.warn("Couldnt save Tardis " + tardis.getUuid());
        }
    }

}
