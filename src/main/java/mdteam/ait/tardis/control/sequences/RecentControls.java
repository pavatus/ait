package mdteam.ait.tardis.control.sequences;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecentControls extends ArrayList<Control> {
	private final UUID tardisId;

	public RecentControls(UUID tardisId, List<Control> controls) {
		super(controls);
		this.tardisId = tardisId;
	}

	public RecentControls(UUID tardisId) {
		this(tardisId, new ArrayList<>());
	}

	public Tardis tardis() {
		if (tardisId == null) {
			AITMod.LOGGER.warn("RecentControls is missing a TARDIS!");
			return null;
		}

		if (TardisUtil.isClient())
			return ClientTardisManager.getInstance().getLookup().get(this.tardisId);

		return ServerTardisManager.getInstance().getTardis(this.tardisId);
	}
}

