package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.tardis.*;

import java.util.UUID;

public class ClientTardis extends Tardis {

    public ClientTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType, ConsoleEnum consoleType) {
        super(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), tardis -> new ClientTardisExterior(tardis, exteriorType), tardis -> new ClientTardisConsole(tardis, consoleType),new DoorHandler(uuid));
    }
}
