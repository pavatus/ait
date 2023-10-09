package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class DimensionUtil {
    public static ServerWorld getTardisDimension(MinecraftServer server) {
        return server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
    }
    public static ServerWorld getTardisDimension() {
        return getTardisDimension(AITMod.mcServer);
    }
}
