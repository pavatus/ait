package the.mdteam.ait.wrapper.server;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisExterior;

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
