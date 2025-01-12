package dev.drtheo.scheduler;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import dev.drtheo.blockqueue.data.TimeUnit;

import net.minecraft.util.Util;

public abstract class BaseScheduler {

    protected final Deque<Task<?>> tasks = new ConcurrentLinkedDeque<>();
    private static final ExecutorService service = Util.getMainWorkerExecutor();

    public Task<?> runTaskLater(Runnable runnable, TimeUnit unit, long delay) {
        return add(new SimpleTask(runnable, TimeUnit.TICKS.from(unit, delay)));
    }

    public Task<?> runAsyncTaskLater(Runnable runnable, TimeUnit unit, long delay) {
        return add(new AsyncSimpleTask(runnable, TimeUnit.TICKS.from(unit, delay)));
    }

    public Task<?> runTaskTimer(Consumer<Task<?>> runnable, TimeUnit unit, long period) {
        return add(new RepeatingSimpleTask(runnable, TimeUnit.TICKS.from(unit, period)));
    }

    public Task<?> runAsyncTaskTimer(Consumer<Task<?>> runnable, TimeUnit unit, long period) {
        return add(new AsyncRepeatingSimpleTask(runnable, TimeUnit.TICKS.from(unit, period)));
    }

    protected Task<?> add(Task<?> task) {
        this.tasks.add(task);
        return task;
    }

    public static abstract class Task<R> {

        protected final R runnable;
        protected boolean cancelled = false;

        public Task(R runnable) {
            this.runnable = runnable;
        }

        /**
         * @return {@literal true} if the task is finished
         */
        protected boolean tick() {
            return this.cancelled;
        }

        public void cancel() {
            this.cancelled = true;
        }

        /**
         * @return {@literal true} if the task should be finished
         */
        public abstract boolean run();
    }

    static abstract class CountUpTask<R> extends Task<R> {

        protected final long target;
        protected long counter;

        public CountUpTask(R runnable, long target) {
            super(runnable);
            this.target = target;
        }

        @Override
        protected boolean tick() {
            if (super.tick())
                return true;

            if (this.counter >= this.target)
                return this.run();

            this.counter++;
            return false;
        }
    }

    static class SimpleTask extends CountUpTask<Runnable> {

        public SimpleTask(Runnable runnable, long delay) {
            super(runnable, delay);
        }

        @Override
        public boolean run() {
            this.runnable.run();
            return true;
        }
    }

    private static class RepeatingSimpleTask extends CountUpTask<Consumer<Task<?>>> {

        public RepeatingSimpleTask(Consumer<Task<?>> runnable, long period) {
            super(runnable, period);
        }

        @Override
        public boolean run() {
            this.runnable.accept(this);
            this.counter = 0;

            return this.cancelled;
        }
    }

    static class AsyncSimpleTask extends SimpleTask {

        public AsyncSimpleTask(Runnable runnable, long delay) {
            super(runnable, delay);
        }

        @Override
        public boolean run() {
            BaseScheduler.service.submit(super::run);
            return true;
        }
    }

    static class AsyncRepeatingSimpleTask extends RepeatingSimpleTask {

        public AsyncRepeatingSimpleTask(Consumer<Task<?>> runnable, long delay) {
            super(runnable, delay);
        }

        @Override
        public boolean run() {
            CompletableFuture<Void> future = new CompletableFuture<Boolean>().completeAsync(super::run).thenAccept(aBoolean -> {
                if (aBoolean)
                    this.cancel();
            });

            BaseScheduler.service.submit(() -> future.get());
            return this.cancelled;
        }
    }
}
