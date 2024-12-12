package loqor.ait.core.util;

import java.util.function.Supplier;

public class Cachable<T> {
    private final Supplier<T> creator;
    private T value;
    private boolean cached;

    public Cachable(Supplier<T> creator) {
        this.creator = creator;

        this.invalidate();
    }

    public T get() {
        if (!cached) {
            value = creator.get();
            cached = true;
        }
        return value;
    }
    public void invalidate() {
        cached = false;
    }
}
