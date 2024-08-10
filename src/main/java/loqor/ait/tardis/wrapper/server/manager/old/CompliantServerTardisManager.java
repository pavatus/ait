package loqor.ait.tardis.wrapper.server.manager.old;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public abstract class CompliantServerTardisManager extends DeprecatedServerTardisManager {

    @Override
    public void sendTardis(TardisComponent component) {
        if (component.tardis() instanceof ServerTardis tardis)
            this.sendTardis(tardis);
    }

    @Override
    public void sendPropertyV2ToSubscribers(Tardis tardis, Value<?> value) {
        this.sendTardis((ServerTardis) tardis);
    }

    @Override
    public void sendToSubscribers(ServerTardis tardis) {
        this.sendTardis(tardis);
    }
}
