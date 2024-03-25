package loqor.ait.tardis.util;

import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.properties.PropertiesHandler;
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
		if (DependencyChecker.hasPortals()) {
			return (PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING))
					|| (tardis.getTravel().getState() == TardisTravel.State.DEMAT)
					|| (tardis.getTravel().getState() == TardisTravel.State.MAT) || (!TardisUtil.getPlayersInInterior(tardis).isEmpty());
		}
		return (PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING))
				|| (tardis.getTravel().getState() == TardisTravel.State.DEMAT)
				|| (tardis.getTravel().getState() == TardisTravel.State.MAT);
	}
}
