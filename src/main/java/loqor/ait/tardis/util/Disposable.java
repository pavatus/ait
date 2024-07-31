package loqor.ait.tardis.util;

public interface Disposable {

    default void age() { }
    void dispose();

    default boolean isAged() {
        return false;
    }
}
