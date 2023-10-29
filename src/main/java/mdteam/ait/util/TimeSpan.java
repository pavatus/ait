package mdteam.ait.util;

public class TimeSpan {
    
    private final long ticks;
    
    private TimeSpan(long ticks) {
        this.ticks = ticks;
    }

    public TimeSpan add(TimeSpan other) {
        return new TimeSpan(this.ticks + other.ticks);
    }

    public long getTicks() {
        return ticks;
    }

    public static TimeSpan ticks(long ticks) {
        return new TimeSpan(ticks);
    }

    public static TimeSpan seconds(long seconds) {
        return new TimeSpan(seconds * 20L);
    }

    public static TimeSpan minutes(long minutes) {
        return TimeSpan.seconds(minutes * 60L);
    }
}
