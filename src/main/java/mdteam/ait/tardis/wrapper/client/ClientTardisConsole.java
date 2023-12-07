package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.tardis.ControlTypes;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;

public class ClientTardisConsole extends TardisConsole {

    public ClientTardisConsole(Tardis tardis, ConsoleEnum console, ControlTypes[] controlTypes) {
        super(tardis, console, controlTypes);
    }
}
