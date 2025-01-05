package dev.drtheo.scheduler;

import dev.pavatus.config.AITConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Scheduler extends BaseScheduler {

    private static Scheduler self;

    private Scheduler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> this.tasks.removeIf(Task::tick));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> this.tasks.clear());
    }

    public static void init() {
        if (self != null)
            return;

        self = new Scheduler();
    }

    public static Scheduler get() {
        return self;
    }
}
