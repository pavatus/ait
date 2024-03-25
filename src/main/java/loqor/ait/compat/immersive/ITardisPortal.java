package loqor.ait.compat.immersive;

import loqor.ait.tardis.Tardis;

import java.util.UUID;

// so that we can set tardis stuff
public interface ITardisPortal {
	void setTardis(UUID id);

	void setTardis(Tardis tardis);
}
