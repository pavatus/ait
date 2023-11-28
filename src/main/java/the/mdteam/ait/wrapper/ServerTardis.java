package the.mdteam.ait.wrapper;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;
import org.apache.logging.log4j.core.jmx.Server;
import the.mdteam.ait.*;

import java.util.UUID;
import java.util.function.Function;


// @TODO warning for this server class and most others, NOTHING will sync. Theo pls do :)
public class ServerTardis extends Tardis {

    public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType) {
        super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), exteriorType);
    }
    public ServerTardis(Tardis tardis) {
        super(tardis.getUuid(), tardis.getTravel().getPosition(),tardis.getDesktop().getSchema(), tardis.getExteriorType());
    }

    @Override
    public void setExteriorType(ExteriorEnum exteriorType) {
        super.setExteriorType(exteriorType);
        this.sync();
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
