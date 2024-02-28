package mdteam.ait.compat;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {
	public static final String IP_MODID = "immersive_portals"; // cant use this for ip cus it doesnt have a modid i think lol, if it does then use that instead seems more future proofed

	public static boolean doesModExist(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	public static boolean hasPortals() {
		return doesModExist("imm_ptl_core");
	}

	public static boolean hasRegeneration() {
		return doesModExist("regen");
	}

	// @TODO add a check to see if the server has the mod or not

	// shoutout to my boy codeium for telling me i can do this love you bro ( please spare me in AI uprise )
	public static boolean doesClassExist(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
