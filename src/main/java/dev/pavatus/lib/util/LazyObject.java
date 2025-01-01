package dev.pavatus.lib.util;

import java.util.function.Supplier;

public class LazyObject<T> {

    private final Supplier<T> supplier;
    private T value;

    public LazyObject(Supplier<T> supplier, T def) {
        this(() -> {
            T b = supplier.get();
            return b == null ? def : b;
        });
    }

    public LazyObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (this.value == null)
            this.value = this.supplier.get();

        return this.value;
    }
}
