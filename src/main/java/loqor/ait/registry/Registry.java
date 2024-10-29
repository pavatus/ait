package loqor.ait.registry;

public interface Registry {
    default void onCommonInit() {
    }

    default void onClientInit() {
    }

    default void onServerInit() {
    }
}
