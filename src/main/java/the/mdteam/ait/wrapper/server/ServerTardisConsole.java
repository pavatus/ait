package the.mdteam.ait.wrapper.server;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisConsole;
import the.mdteam.ait.TardisExterior;

public class ServerTardisConsole extends TardisConsole {

    public ServerTardisConsole(Tardis tardis, ConsoleEnum console) {
        super(tardis, console);
    }

    @Override
    public void setType(ConsoleEnum console) {
        super.setType(console);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
