package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;

public class ServerTardisExterior extends TardisExterior {

    public ServerTardisExterior(Tardis tardis, ExteriorEnum exterior, boolean locked) {
        super(tardis, exterior, locked);
    }

    @Override
    public void setType(ExteriorEnum exterior) {
        super.setType(exterior);
        this.sync();
    }

    @Override
    public void setLocked(boolean locked) {
        super.setLocked(locked);
        this.sync();
    }

    public void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
