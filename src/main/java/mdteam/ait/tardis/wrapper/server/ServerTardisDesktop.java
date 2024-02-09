package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;

public class ServerTardisDesktop extends TardisDesktop implements TardisTickable {

    public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        super(tardis, schema);
    }

}
