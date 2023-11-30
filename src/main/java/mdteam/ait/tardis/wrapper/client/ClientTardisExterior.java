package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.Tardis;

public class ClientTardisExterior extends TardisExterior {

    public ClientTardisExterior(Tardis tardis, ExteriorEnum exterior, boolean locked) {
        super(tardis, exterior, locked);
    }
}
