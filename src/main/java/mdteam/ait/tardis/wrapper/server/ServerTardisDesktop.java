package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisTickable;

public class ServerTardisDesktop extends TardisDesktop implements TardisTickable {

	public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
		super(tardis, schema);
	}

}
