package mdteam.ait.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class DependencyChecker {

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
