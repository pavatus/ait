package mdteam.ait.api.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;

import java.util.UUID;

public interface ITardis {

    UUID getUuid();

    void setDesktop(IDesktop desktop);
    IDesktop getDesktop();

    void setExteriorType(ExteriorEnum exteriorType);
    ExteriorEnum getExteriorType();

    ITravel getTravel();
}
