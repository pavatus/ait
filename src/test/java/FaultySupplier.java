import java.util.function.Supplier;

@FunctionalInterface
public interface FaultySupplier<T> {
    T get() throws Exception;
}
