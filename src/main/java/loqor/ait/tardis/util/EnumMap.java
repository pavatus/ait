package loqor.ait.tardis.util;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Custom and lightweight map implementation for enums. I know {@link java.util.EnumMap} exists, but it's different.
 */
public class EnumMap<K extends Ordered, V> {

    private final V[] values;

    public EnumMap(Supplier<K[]> values, Function<Integer, V[]> supplier) {
        this.values = supplier.apply(values.get().length);
    }

    public void put(K k, V v) {
        values[k.index()] = v;
    }

    public V get(K k) {
        return values[k.index()];
    }

    public boolean containsKey(K k) {
        return this.values[k.index()] != null;
    }

    /**
     * @implNote Will return ALL values, including nulls.
     * @return All values associated with each variant of an enum, null if no value is present.
     */
    public V[] values() {
        return this.values;
    }
    
    public void clear() {
        Arrays.fill(this.values, null);
    }
}
