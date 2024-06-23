package loqor.ait.client.screens;

import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.UUID;

public abstract class TardisScreen extends Screen {
	UUID tardisId;

	protected TardisScreen(Text title, UUID tardis) {
		super(title);
		this.tardisId = tardis;
	}

	public ClientTardis tardis() {
		return this.getFromUUID(this.tardisId);
	}

	protected ClientTardis getFromUUID(UUID tardisid) {
		return ClientTardisManager.getInstance().demandTardis(tardisid);
	}
}
