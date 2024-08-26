package loqor.ait.api.link;

import java.util.Optional;

import loqor.ait.core.tardis.Tardis;

public interface Linkable {
    Optional<Tardis> findTardis(boolean isClient);

    void setTardis(Tardis tardis);
}
