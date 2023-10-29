package mdteam.ait.util;

import mdteam.ait.AITMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.HashSet;
import java.util.Set;

public class Scheduler {

    private static final Set<Scheduler> schedulers = new HashSet<>();

    protected final TimeSpan span;
    protected final Runnable runnable;

    protected long ticks = 0;

    /**
     * Executes the runnable every period of time declared in span.
     * @param span the time period
     * @param runnable code that needs to be executed
     */
    private Scheduler(TimeSpan span, Runnable runnable) {
        this.span = span;
        this.runnable = runnable;
    }

    public static void schedule(TimeSpan span, Runnable runnable) {
        schedulers.add(new Scheduler(span, runnable));
    }

    public static void scheduleOnce(TimeSpan span, Runnable runnable) {
        schedulers.add(new TempScheduler(span, runnable));
    }

    public void tick() {
        AITMod.LOGGER.info("Ticking scheduler: {}", this);

        this.ticks++;
        this.check();
    }

    public void check() {
        if (this.ticks >= this.span.getTicks()) {
            this.ticks = 0;
            this.trigger();
        }
    }

    public void trigger() {
        this.runnable.run();
    }

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (Scheduler scheduler : schedulers) {
                scheduler.tick();
            }
        });
    }

    static class TempScheduler extends Scheduler {

        /**
         * Executes the runnable every period of time declared in span.
         *
         * @param span     the time period
         * @param runnable code that needs to be executed
         */
        public TempScheduler(TimeSpan span, Runnable runnable) {
            super(span, runnable);
        }

        @Override
        public void trigger() {
            schedulers.remove(this);
            super.trigger();
        }
    }
}
