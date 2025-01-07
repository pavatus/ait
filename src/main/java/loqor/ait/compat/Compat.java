package loqor.ait.compat;

import net.fabricmc.api.ClientModInitializer;

import loqor.ait.api.AITModInitializer;
import loqor.ait.compat.gravity.GravityHandler;

public class Compat implements AITModInitializer, ClientModInitializer {

    @Override
    public void onInitializeAIT() {
        if (DependencyChecker.hasGravity())
            GravityHandler.init();
    }

    @Override
    public void onInitializeClient() {
        if (DependencyChecker.hasGravity())
            GravityHandler.clientInit();
    }
}
