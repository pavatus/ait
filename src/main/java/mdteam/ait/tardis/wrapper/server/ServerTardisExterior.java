package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;

public class ServerTardisExterior extends TardisExterior {

    public ServerTardisExterior(Tardis tardis, ExteriorEnum exterior, VariantEnum variant) {
        super(tardis, exterior, variant);
    }

    @Override
    public void setType(ExteriorEnum exterior) {
        super.setType(exterior);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }

    @Override
    public void setVariant(VariantEnum variant) {
        super.setVariant(variant);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
