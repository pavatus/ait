package loqor.ait.client.screens;

import loqor.ait.tardis.link.v2.TardisRef;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class TardisScreen extends Screen {

	private final TardisRef ref;

	protected TardisScreen(Text title, ClientTardis tardis) {
		super(title);

		this.ref = new TardisRef(tardis, uuid -> ClientTardisManager.getInstance().demandTardis(uuid));
	}

	public ClientTardis tardis() {
		return (ClientTardis) this.ref.get();
	}
}
