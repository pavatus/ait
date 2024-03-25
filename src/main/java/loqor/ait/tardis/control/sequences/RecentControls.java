package loqor.ait.tardis.control.sequences;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

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

