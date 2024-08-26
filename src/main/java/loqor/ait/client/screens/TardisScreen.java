package loqor.ait.client.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import loqor.ait.api.link.v2.TardisRef;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.core.tardis.manager.ClientTardisManager;

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
