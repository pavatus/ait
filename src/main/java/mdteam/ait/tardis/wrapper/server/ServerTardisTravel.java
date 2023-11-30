package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

public class ServerTardisTravel extends TardisTravel {

    public ServerTardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis, pos);
    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        super.setDestination(pos, withChecks);
        this.sync();
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        super.setPosition(pos);
        this.sync();
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        this.sync();
    }

    @Override
    public void dematerialise(boolean withRemat) {
        super.dematerialise(withRemat);
        this.sync();
    }

    @Override
    public void materialise() {
        super.materialise();
        this.sync();
    }

    public void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
