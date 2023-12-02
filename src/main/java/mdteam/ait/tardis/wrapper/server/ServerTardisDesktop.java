package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.ITardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;

public class ServerTardisDesktop extends TardisDesktop {

    public ServerTardisDesktop(ITardis tardis, TardisDesktopSchema schema) {
        super(tardis, schema);
    }

    @Override
    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        super.setInteriorDoorPos(pos);

        ServerTardisManager.getInstance().sendToSubscribers(this);
    }
}
