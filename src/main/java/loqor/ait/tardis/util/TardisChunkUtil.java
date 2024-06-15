package loqor.ait.tardis.util;

import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.tardis.TardisTravel;
import net.minecraft.server.world.ServerWorld;

public class TardisChunkUtil {
	public static void forceLoadExteriorChunk(Tardis tardis) {
		if (!(tardis.travel().getPosition().getWorld() instanceof ServerWorld)) return;

		ForcedChunkUtil.keepChunkLoaded((ServerWorld) tardis.travel().getPosition().getWorld(), tardis.travel().getPosition());
	}

	public static void stopForceExteriorChunk(Tardis tardis) {
		if (!(tardis.travel().getPosition().getWorld() instanceof ServerWorld)) return;

		ForcedChunkUtil.stopForceLoading((ServerWorld) tardis.travel().getPosition().getWorld(), tardis.travel().getPosition());
	}

	public static boolean isExteriorChunkForced(Tardis tardis) {
		if (!(tardis.travel().getPosition().getWorld() instanceof ServerWorld)) return false;

		return ForcedChunkUtil.isChunkForced((ServerWorld) tardis.travel().getPosition().getWorld(), tardis.travel().getPosition());
	}

	public static boolean shouldExteriorChunkBeForced(Tardis tardis) {
		if (!(tardis.travel().getPosition().getWorld() instanceof ServerWorld)) return false;
		if (DependencyChecker.hasPortals()) {
			return (PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING))
					|| (tardis.travel().getState() == TardisTravel.State.DEMAT)
					|| (tardis.travel().getState() == TardisTravel.State.MAT) || (!TardisUtil.getPlayersInInterior(tardis).isEmpty());
		}
		return (PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING))
				|| (tardis.travel().getState() == TardisTravel.State.DEMAT)
				|| (tardis.travel().getState() == TardisTravel.State.MAT);
	}
}
