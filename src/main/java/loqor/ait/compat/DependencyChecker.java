package loqor.ait.compat;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {

	private static final boolean HAS_PORTALS = doesModExist("imm_ptl_core");
	private static final boolean HAS_IRIS = doesModExist("iris");
	private static final boolean HAS_GRAVITY = doesModExist("gravity_changer_q");

	public static boolean doesModExist(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	public static boolean hasPortals() {
		return HAS_PORTALS;
	}

	public static boolean hasIris() {
		return HAS_IRIS;
	}

	public static boolean hasGravity() {
		return HAS_GRAVITY;
	}
}
