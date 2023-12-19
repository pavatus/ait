package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;

import java.util.UUID;


public class ServerTardis extends Tardis {

    public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType, VariantEnum variantType, ConsoleEnum consoleType, boolean locked) {
        super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), tardis -> new ServerTardisExterior(tardis, exteriorType, variantType), tardis -> new ServerTardisConsole(tardis, consoleType, consoleType.getControlTypesList()), locked);
    }

    @Override
    public void setDesktop(TardisDesktop desktop) {
        super.setDesktop(desktop);
        this.sync();
    }

    public void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this);
    }
}