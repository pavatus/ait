package loqor.ait.tardis.link;

import loqor.ait.tardis.Tardis;

import java.util.Optional;

public interface Linkable {
	Optional<Tardis> findTardis(boolean isClient);

	void setTardis(Tardis tardis);
}
