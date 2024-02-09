package mdteam.ait.tardis.link;

import mdteam.ait.tardis.Tardis;

import java.util.Optional;

public interface Linkable {
    Optional<Tardis> findTardis();
    void setTardis(Tardis tardis);

    /**
     * If false, calling {@link Linkable#setTardis(Tardis)} might throw an exception!
     */
    default boolean linkable() {
        return true;
    }
}
