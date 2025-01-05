package dev.drtheo.scheduler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Scheduler extends BaseScheduler {

    private static Scheduler self;

    private Scheduler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> this.tasks.removeIf(Task::tick));
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
