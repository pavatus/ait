package dev.drtheo.blockqueue.impl;

import java.util.function.Supplier;

import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.scheduler.Scheduler;

public class StepQueue {

    // TODO: use completablefutures instead
    public static void scheduleSteps(Step step, int period, int maxTime, Runnable finish) {
        Scheduler.get().runTaskTimer(t -> {
            boolean shouldContinue = true;
            long start = System.currentTimeMillis();

            while (shouldContinue) {
                shouldContinue = System.currentTimeMillis() - start < maxTime;

                if (step.get()) {
                    t.cancel();
                    finish.run();
                    return;
                }
            }
        }, TimeUnit.TICKS, period);
    }

    public interface Step extends Supplier<Boolean> { }
}
