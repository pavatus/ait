package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;

public class ServerTardisDesktop extends TardisDesktop {

    public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        super(tardis, schema);
    }

    @Override
    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        super.setInteriorDoorPos(pos);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }

    @Override
    public void setConsolePos(AbsoluteBlockPos.Directed pos) {
        super.setConsolePos(pos);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
