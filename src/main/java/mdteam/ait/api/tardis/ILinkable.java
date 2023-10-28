package mdteam.ait.api.tardis;

public interface ILinkable {

    ITardis getTardis();
    void setTardis(ITardis tardis);

    /**
     * If false, calling {@link ILinkable#setTardis(ITardis)} might throw an exception!
     */
    default boolean linkable() {
        return true;
    }
}
