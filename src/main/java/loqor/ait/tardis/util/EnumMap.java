package loqor.ait.tardis.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Custom and lightweight map implementation for enums. I know
 * {@link java.util.EnumMap} exists, but it's different.
 */
public class EnumMap<K extends Ordered, V> implements Map<K, V> {

    private final V[] values;

    public EnumMap(Supplier<K[]> values, Function<Integer, V[]> supplier) {
        this.values = supplier.apply(values.get().length);
    }

    public void apply(Function<V, V> func) {
        for (int i = 0; i < values.length; i++) {
            values[i] = func.apply(values[i]);
        }


    }

    /**
     * @implNote Will return ALL values, including nulls.
     * @return All values associated with each variant of an enum, null if no value
     *         is present.
     */
    public V[] getValues() {
        return this.values;
    }

    @Override
    public V put(K k, V v) {
        V prev = values[k.index()];
        values[k.index()] = v;

        return prev;
    }

    public V remove(K k) {
        V prev = values[k.index()];
        values[k.index()] = null;

        return prev;
    }

    public V get(K k) {
        return this.get(k.index());
    }

    public V get(int index) {
        return values[index];
    }

    public boolean containsKey(K k) {
        return this.values[k.index()] != null;
    }

    @Override
    public void clear() {
        Arrays.fill(this.values, null);
    }

    //region Map compliant code

    @Override
    public V remove(Object k) {
        return this.remove((K) k);
    }

    @Override
    public V get(Object key) {
        return this.get((K) key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public int size() {
        return this.values.length;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.containsKey((K) key);
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Set<K> keySet() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return List.of(this.values);
    }

    @Override
    public boolean isEmpty() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion
}
