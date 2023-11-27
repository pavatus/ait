package the.mdteam.ait.wrapper;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisDesktop;
import the.mdteam.ait.TardisDesktopSchema;

import java.util.UUID;

public class ServerTardis extends Tardis {

    public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType) {
        super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), exteriorType);
    }
    public ServerTardis(Tardis tardis) {
        this(tardis.getUuid(), tardis.getTravel().getPosition(),tardis.getDesktop().getSchema(), tardis.getExteriorType());
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

    // @TODO have to do this as ServerTardis and allat is not properly used, waiting on theo to finish so heres jank
    public void duzoJankSync() {
        Tardis realTardis = ServerTardisManager.getInstance().getTardis(this.getUuid());

        realTardis.getTravel().setDestination(this.getTravel().getDestination(), true);
        realTardis.getTravel().setPosition(this.getTravel().getPosition());
        realTardis.getTravel().setState(this.getTravel().getState());
    }
}
