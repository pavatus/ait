package the.mdteam.ait.wrapper;

import mdteam.ait.data.AbsoluteBlockPos;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;

public class ServerTardisTravel extends TardisTravel {

    public ServerTardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis, pos);
    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        super.setDestination(pos, withChecks);
        ((ServerTardis) this.tardis).sync();
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        super.setPosition(pos);
        ((ServerTardis) this.tardis).sync();
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        ((ServerTardis) this.tardis).sync();
    }
}
