package dev.drtheo.blockqueue;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import dev.drtheo.blockqueue.api.Finishable;
import org.jetbrains.annotations.Nullable;

public class ActionQueue implements Finishable {

    private final Deque<Consumer<Finishable>> steps = new ConcurrentLinkedDeque<>();

    public ActionQueue() {

    }

    public ActionQueue thenRun(ActionQueue other) {
        return thenRun(f -> other.thenRun(f::finish).execute());
    }

    public ActionQueue thenRun(Optional<ActionQueue> other) {
        if (other.isEmpty())
            return this;

        return thenRun(other.get());
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

    public ActionQueue firstRun(Consumer<Finishable> consumer) {
        this.steps.addFirst(consumer);
        return this;
    }

    public ActionQueue firstRun(@Nullable Runnable runnable) {
        if (runnable == null)
            return this;

        return this.firstRun(f -> {
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
