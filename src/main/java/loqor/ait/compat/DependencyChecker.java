package loqor.ait.compat;

import com.mojang.blaze3d.platform.GlDebugInfo;
import dev.pavatus.lib.util.LazyObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {

    private static final boolean HAS_PORTALS = doesModExist("imm_ptl_core");
    private static final boolean HAS_IRIS = doesModExist("iris");
    private static final boolean HAS_GRAVITY = doesModExist("gravity_changer_q");

    @Environment(EnvType.CLIENT)
    private static final LazyObject<Boolean> NVIDIA_CARD = new LazyObject<>(
            () -> GlDebugInfo.getVendor().toLowerCase().contains("nvidia"));

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

    @Environment(EnvType.CLIENT)
    public static boolean hasNvidiaCard() {
        return NVIDIA_CARD.get();
    }
}
