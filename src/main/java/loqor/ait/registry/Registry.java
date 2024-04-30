package loqor.ait.registry;

public interface Registry {
    /**
     * @deprecated See {@link Registry#onCommonInit()}
     */
    @Deprecated
    default void init() {
        this.onCommonInit();
    }

    default void onCommonInit() { }
    default void onClientInit() { }
    default void onServerInit() { }
}
