package loqor.ait.api.link;

import java.util.Optional;

import loqor.ait.core.tardis.Tardis;

/**
 * @deprecated Use {@link loqor.ait.api.link.v2.Linkable} instead.
 */
@Deprecated(since = "1.2.0", forRemoval = true)
public interface Linkable {
    Optional<Tardis> findTardis(boolean isClient);

    void setTardis(Tardis tardis);
}
