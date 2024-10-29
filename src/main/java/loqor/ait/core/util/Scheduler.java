package loqor.ait.core.util;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.util.Util;

import loqor.ait.data.TimeUnit;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Scheduler {

    private static Scheduler self;
    private final Deque<Task> tasks = new ConcurrentLinkedDeque<>();
    private final ExecutorService service = Util.getMainWorkerExecutor();

    private Scheduler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> this.tasks.removeIf(Task::tryTick));
    }

    public static void init() {
        self = new Scheduler();
    }

    public static Task runTaskLater(Runnable runnable, TimeUnit unit, long delay) {
        return add(new SimpleTask(runnable, TimeUnit.TICKS.from(unit, delay)));
    }

    public static Task runAsyncTaskLater(Runnable runnable, TimeUnit unit, long delay) {
        return add(new AsyncSimpleTask(runnable, TimeUnit.TICKS.from(unit, delay)));
    }

    public static Task runTaskTimer(Runnable runnable, TimeUnit unit, long period) {
        return add(new RepeatingSimpleTask(runnable, TimeUnit.TICKS.from(unit, period)));
    }

    public static Task runAsyncTaskTimer(Runnable runnable, TimeUnit unit, long period) {
        return add(new AsyncRepeatingSimpleTask(runnable, TimeUnit.TICKS.from(unit, period)));
    }

    private static Task add(Task task) {
        self.tasks.add(task);
        return task;
    }

    public static abstract class Task {

        private final Runnable runnable;
        private boolean cancelled = false;
        private boolean done = false;

        public Task(Runnable runnable) {
            this.runnable = runnable;
        }

        public boolean tryTick() {
            boolean result = this.tick();

            if (result)
                this.done = true;

            return result;
        }

        protected boolean tick() {
            return this.cancelled;
        }

        public void cancel() {
            this.cancelled = true;
        }

        public boolean run() {
            this.runnable.run();
            return true;
        }

        public boolean done() {
            return done;
        }
    }

    private static class SimpleTask extends Task {

        protected final long delay;
        protected long counter;

        public SimpleTask(Runnable runnable, long delay) {
            super(runnable);

            this.delay = delay;
        }

        @Override
        protected boolean tick() {
            if (super.tick())
                return true;

            if (this.counter == this.delay)
                return this.run();

            this.counter++;
            return false;
        }
    }

    private static class RepeatingSimpleTask extends SimpleTask {

        public RepeatingSimpleTask(Runnable runnable, long delay) {
            super(runnable, delay);
        }

        @Override
        public boolean run() {
            super.run();
            this.counter = 0;

            return false;
        }
    }

    static class AsyncSimpleTask extends SimpleTask {

        public AsyncSimpleTask(Runnable runnable, long delay) {
            super(runnable, delay);
        }

        @Override
        public boolean run() {
            Scheduler.self.service.submit(super::run);
            return true;
        }
    }

    static class AsyncRepeatingSimpleTask extends RepeatingSimpleTask {

        public AsyncRepeatingSimpleTask(Runnable runnable, long delay) {
            super(runnable, delay);
        }

        @Override
        public boolean run() {
            Scheduler.self.service.submit(super::run);
            return false;
        }
    }
}
