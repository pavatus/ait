package dev.drtheo.scheduler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ClientScheduler extends BaseScheduler {

    private static ClientScheduler self;

    private ClientScheduler() {
        ClientTickEvents.END_CLIENT_TICK.register(server -> this.tasks.removeIf(Task::tick));
    }

    public static void init() {
        if (self != null)
            return;

        self = new ClientScheduler();
    }

    public static ClientScheduler get() {
        return self;
    }
}
