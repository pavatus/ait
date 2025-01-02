package dev.pavatus.multidim;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultidimMod implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("ait-multidim");

    @Override
    public void onInitialize() {
        Multidim.init();
    }
}
