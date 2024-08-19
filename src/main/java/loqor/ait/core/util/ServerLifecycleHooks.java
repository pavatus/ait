package loqor.ait.core.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerLifecycleHooks {

    private static MinecraftServer server;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerLifecycleHooks.server = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleHooks.server = null);
    }

    public static MinecraftServer get() {
        return server;
    }
}
