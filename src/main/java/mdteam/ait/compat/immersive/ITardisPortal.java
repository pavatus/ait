package mdteam.ait.compat.immersive;

import mdteam.ait.tardis.Tardis;

import java.util.UUID;

// so that we can set tardis stuff
public interface ITardisPortal {
    void setTardis(UUID id);
    void setTardis(Tardis tardis);
}
