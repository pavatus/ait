package dev.drtheo.blockqueue.util;

import java.util.function.Supplier;

import dev.drtheo.blockqueue.api.Finishable;
import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.scheduler.Scheduler;
import org.jetbrains.annotations.Nullable;

public class StepUtil {

    /**
     * Schedules a task to run {@literal step} every {@literal period} of a {@literal unit}.
     * Executes {@literal callback} when finishes.
     */
    public static void scheduleSteps(@Nullable Finishable callback, Step step, TimeUnit unit, int period, int maxTime) {
        Scheduler.get().runTaskTimer(t -> {
            boolean shouldContinue = true;
            long start = System.currentTimeMillis();

            while (shouldContinue) {
                shouldContinue = System.currentTimeMillis() - start < maxTime;

                if (step.get()) {
                    t.cancel();

                    if (callback != null)
                        callback.finish();

                    return;
                }
            }
        }, unit, period);
    }

    public interface Step extends Supplier<Boolean> { }
}
