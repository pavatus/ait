package mdteam.ait.core.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public class ServerLoadEvent {

    private static boolean first = true;

    public static final Event<Load> LOAD = EventFactory.createArrayBacked(ServerLoadEvent.Load.class, callbacks -> (server) -> {
        if (!first)
            return;

        for (Load callback : callbacks) {
            callback.onServerLoad(server);
        }

        first = false;
    });

    @FunctionalInterface
    public interface Load {
        void onServerLoad(MinecraftServer server);
    }
}