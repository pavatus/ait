package loqor.ait.tardis.wrapper.server;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.base.TardisTickable;

public class ServerTardisDesktop extends TardisDesktop implements TardisTickable {

	public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
		super(tardis, schema);
	}

}
