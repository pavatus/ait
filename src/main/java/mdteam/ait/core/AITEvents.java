package mdteam.ait.core;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.World;
import mdteam.ait.core.events.ServerLoadEvent;

public class AITEvents {

    public static void init() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey() == World.OVERWORLD) {
                ServerLoadEvent.LOAD.invoker().onServerLoad(server);
            }
        });
    }
}
