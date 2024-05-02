package loqor.ait.core.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.WorldSavePath;

public class WorldUtil {

    /**
     * Client-side only!
     */
    public static String getName(MinecraftClient client) {
        if (client.isInSingleplayer()) {
            return client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
        }

        return client.getCurrentServerEntry().address;
    }
}
