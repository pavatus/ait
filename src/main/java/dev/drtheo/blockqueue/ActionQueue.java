package dev.drtheo.blockqueue;

import dev.drtheo.blockqueue.api.Finishable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class ActionQueue implements Finishable {

    private final Deque<Consumer<Finishable>> steps = new ArrayDeque<>();

    public ActionQueue() {

    }

    public ActionQueue thenRun(Consumer<Finishable> consumer) {
        this.steps.add(consumer);
        return this;
    }

    public ActionQueue thenRun(@Nullable Runnable runnable) {
        if (runnable == null)
            return this;

        return this.thenRun(f -> {
            runnable.run();
            f.finish();
        });
    }

    public ActionQueue execute() {
        Consumer<Finishable> consumer = this.steps.poll();

        if (consumer != null)
            consumer.accept(this);

        return this;
    }

    @Override
    public void finish() {
        this.execute();
    }
}
