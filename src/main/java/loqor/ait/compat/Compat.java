package loqor.ait.compat;

import loqor.ait.api.AITModInitializer;
import loqor.ait.compat.gravity.GravityHandler;
import loqor.ait.compat.immersive.PortalsHandler;
import net.fabricmc.api.ClientModInitializer;

public class Compat implements AITModInitializer, ClientModInitializer {

    @Override
    public void onInitializeAIT() {
        if (DependencyChecker.hasGravity())
            GravityHandler.init();

        if (DependencyChecker.hasPortals())
            PortalsHandler.init();
    }

    @Override
    public void onInitializeClient() {
        if (DependencyChecker.hasGravity())
            GravityHandler.clientInit();
    }
}
