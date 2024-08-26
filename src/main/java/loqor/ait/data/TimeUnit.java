package loqor.ait.data;

public enum TimeUnit {

    NANOSECONDS(TimeUnit.NANO_SCALE),
    MICROSECONDS(TimeUnit.MICRO_SCALE),
    MILLISECONDS(TimeUnit.MILLI_SCALE),
    TICKS(TimeUnit.TICK_SCALE),
    SECONDS(TimeUnit.SECOND_SCALE),
    MINUTES(TimeUnit.MINUTE_SCALE);

    private static final long NANO_SCALE = 1L;
    private static final long MICRO_SCALE = 1000L * NANO_SCALE;
    private static final long MILLI_SCALE = 1000L * MICRO_SCALE;
    private static final long TICK_SCALE = 50L * MILLI_SCALE;
    private static final long SECOND_SCALE = 1000L * MILLI_SCALE;
    private static final long MINUTE_SCALE = 60L * SECOND_SCALE;

    private final long scale;
    
    TimeUnit(long scale) {
        this.scale = scale;
    }

    public long from(TimeUnit from, long time) {
        if (this == from)
            return time;

        return from.scale * time / this.scale;
    }
}
