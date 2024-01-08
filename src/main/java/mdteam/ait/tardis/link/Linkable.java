package mdteam.ait.tardis.link;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.DoorData;

public interface Linkable {
    Tardis getTardis();
    void setTardis(Tardis tardis);
    default TardisDesktop getDesktop() { return this.getTardis().getDesktop(); }
    default TardisTravel getTravel() { return this.getTardis().getTravel(); }

    /**
     * If false, calling {@link Linkable#setTardis(Tardis)} might throw an exception!
     */
    default boolean linkable() {
        return true;
    }
}
