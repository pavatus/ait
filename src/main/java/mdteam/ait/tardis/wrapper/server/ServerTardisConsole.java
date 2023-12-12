package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;

public class ServerTardisConsole extends TardisConsole {

    public ServerTardisConsole(Tardis tardis, ConsoleEnum console, ControlTypes[] controlTypes) {
        super(tardis, console, controlTypes);
    }

    @Override
    public void setControlTypes(ControlTypes[] controlTypes) {
        super.setControlTypes(controlTypes);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }

    @Override
    public void setType(ConsoleEnum console) {
        super.setType(console);

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
