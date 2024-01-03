package mdteam.ait.client.screens;

import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.Tardis;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.UUID;

public abstract class TardisScreen extends Screen {
    UUID tardisId;

    protected TardisScreen(Text title, UUID tardis) {
        super(title);
        this.tardisId = tardis;
    }

    protected Tardis tardis() {
        return ClientTardisManager.getInstance().getLookup().get(tardisId);
    }

    protected Tardis getFromUUID(UUID tardisid) {
        return ClientTardisManager.getInstance().getLookup().get(tardisid);
    }

    protected Tardis updateTardis() {
        ClientTardisManager.getInstance().ask(this.tardisId);
        return tardis();
    }
}
