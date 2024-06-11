package loqor.ait.compat;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {
	public static final String IP_MODID = "immersive_portals"; // cant use this for ip cus it doesnt have a modid i think lol, if it does then use that instead seems more future proofed
	private static final boolean HAS_PORTALS = doesModExist("imm_ptl_core");
	private static final boolean HAS_IRIS = doesModExist("iris");

	public static boolean doesModExist(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	public static boolean hasPortals() {
		return HAS_PORTALS;
	}

	public static boolean hasIris() {
		return HAS_IRIS;
	}
}
