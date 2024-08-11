package loqor.ait.tardis.link;

import java.util.Optional;

import loqor.ait.tardis.Tardis;

public interface Linkable {
    Optional<Tardis> findTardis(boolean isClient);

    void setTardis(Tardis tardis);
}
