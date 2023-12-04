package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.tardis.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;

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
