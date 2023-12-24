package mdteam.ait.tardis.util;

import mdteam.ait.core.util.ForcedChunkUtil;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.world.ServerWorld;

public class TardisChunkUtil {
    public static void forceLoadExteriorChunk(Tardis tardis) {
        if (!(tardis.getTravel().getPosition().getWorld() instanceof ServerWorld)) return;

        ForcedChunkUtil.keepChunkLoaded((ServerWorld) tardis.getTravel().getPosition().getWorld(), tardis.getTravel().getPosition());
    }
    public static void stopForceExteriorChunk(Tardis tardis) {
        if (!(tardis.getTravel().getPosition().getWorld() instanceof ServerWorld)) return;

        ForcedChunkUtil.stopForceLoading((ServerWorld) tardis.getTravel().getPosition().getWorld(), tardis.getTravel().getPosition());
    }
    public static boolean isExteriorChunkForced(Tardis tardis) {
        if (!(tardis.getTravel().getPosition().getWorld() instanceof ServerWorld)) return false;

        return ForcedChunkUtil.isChunkForced((ServerWorld) tardis.getTravel().getPosition().getWorld(), tardis.getTravel().getPosition());
    }

    public static boolean shouldExteriorChunkBeForced(Tardis tardis) {
        if (!(tardis.getTravel().getPosition().getWorld() instanceof ServerWorld)) return false;

        return (PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING))
                || (tardis.getTravel().getState() == TardisTravel.State.DEMAT)
                || (tardis.getTravel().getState() == TardisTravel.State.MAT)
                || (!TardisUtil.getPlayersInInterior(tardis).isEmpty());
    }
}
