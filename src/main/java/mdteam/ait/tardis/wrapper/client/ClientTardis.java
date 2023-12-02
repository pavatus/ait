package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.*;

import java.util.UUID;

public class ClientTardis extends Tardis {

    public ClientTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType) {
        super(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), tardis -> new ClientTardisExterior(tardis, exteriorType), TardisDoor::new);
    }
}
