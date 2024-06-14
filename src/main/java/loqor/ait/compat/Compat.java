package loqor.ait.compat;

import loqor.ait.api.AITModInitializer;
import loqor.ait.compat.gravity.GravityHandler;

public class Compat implements AITModInitializer {

    @Override
    public void onInitializeAIT() {
        if (DependencyChecker.hasGravity())
            GravityHandler.init();
    }
}
