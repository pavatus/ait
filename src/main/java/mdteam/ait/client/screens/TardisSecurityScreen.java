package mdteam.ait.client.screens;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TardisSecurityScreen extends BaseOwoScreen<FlowLayout> {
    private final UUID tardisID;

    public TardisSecurityScreen(UUID tardisID) {
        this.tardisID = tardisID;
        this.updateTardis();
    }

    protected Tardis updateTardis() {
        ClientTardisManager.getInstance().ask(this.tardisID);
        return tardis();
    }

    protected Tardis tardis() {
        return ClientTardisManager.getInstance().getLookup().get(this.tardisID);
    }
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return null;
    }

    @Override
    protected void build(FlowLayout rootComponent) {

    }
}
