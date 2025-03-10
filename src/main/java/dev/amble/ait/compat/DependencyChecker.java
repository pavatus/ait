package dev.amble.ait.compat;

import com.mojang.blaze3d.platform.GlDebugInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {

    private static final boolean HAS_PORTALS = doesModExist("imm_ptl_core");
    private static final boolean HAS_IRIS = doesModExist("iris");
    private static final boolean HAS_SODIUM = doesModExist("sodium");
    private static final boolean HAS_GRAVITY = doesModExist("gravity_changer_q");
    private static final boolean HAS_PATCHOULI = doesModExist("patchouli");

    private static Boolean NVIDIA_CARD;

    public static boolean doesModExist(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public static boolean hasPortals() {
        return HAS_PORTALS;
    }

    public static boolean hasSodium() {
        return HAS_SODIUM;
    }

    public static boolean hasIris() {
        return HAS_IRIS;
    }

    public static boolean hasGravity() {
        return HAS_GRAVITY;
    }
    public static boolean hasPatchouli() {
        return HAS_PATCHOULI;
    }

    @Environment(EnvType.CLIENT)
    public static boolean hasNvidiaCard() {
        if (NVIDIA_CARD == null)
            NVIDIA_CARD = GlDebugInfo.getVendor().toLowerCase().contains("nvidia");

        return NVIDIA_CARD;
    }
}
