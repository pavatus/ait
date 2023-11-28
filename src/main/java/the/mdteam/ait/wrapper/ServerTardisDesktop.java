package the.mdteam.ait.wrapper;

import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisDesktop;
import the.mdteam.ait.TardisDesktopSchema;

public class ServerTardisDesktop extends TardisDesktop {

    public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        super(tardis, schema);
    }
    public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema, Corners corners, AbsoluteBlockPos.Directed doors) {
        super(tardis, schema,corners,doors);
    }

    @Override
    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        super.setInteriorDoorPos(pos);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
