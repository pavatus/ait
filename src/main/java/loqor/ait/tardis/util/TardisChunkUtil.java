package loqor.ait.tardis.util;

import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TravelHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;

public class TardisChunkUtil {

	public static void forceLoadExteriorChunk(Tardis tardis) {
		ForcedChunkUtil.keepChunkLoaded(tardis.travel().position());
	}

	public static void stopForceExteriorChunk(Tardis tardis) {
		ForcedChunkUtil.stopForceLoading(tardis.travel().position());
	}

	public static boolean isExteriorChunkForced(Tardis tardis) {
		return ForcedChunkUtil.isChunkForced(tardis.travel().position());
	}

	public static boolean shouldExteriorChunkBeForced(Tardis tardis) {
		if (DependencyChecker.hasPortals()) {
			return !(PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_FALLING)
					|| tardis.travel().getState() == TravelHandler.State.DEMAT
					|| tardis.travel().getState() == TravelHandler.State.MAT
					|| !TardisUtil.getPlayersInInterior(tardis).isEmpty());
		}

		return !(PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_FALLING)
				|| tardis.travel().getState() == TravelHandler.State.DEMAT
				|| tardis.travel().getState() == TravelHandler.State.MAT);
	}
}
