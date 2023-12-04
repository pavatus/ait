package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.tardis.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;

public class ServerTardisExterior extends TardisExterior {

    public ServerTardisExterior(Tardis tardis, ExteriorEnum exterior) {
        super(tardis, exterior);
    }

    @Override
    public void setType(ExteriorEnum exterior) {
        super.setType(exterior);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
