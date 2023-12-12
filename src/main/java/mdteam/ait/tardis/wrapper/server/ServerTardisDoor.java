package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.ITardis;
import mdteam.ait.tardis.TardisDoor;
import mdteam.ait.tardis.linkable.Linkable;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;

public class ServerTardisDoor extends TardisDoor {

    public ServerTardisDoor(ITardis tardis) {
        super(tardis);
    }

    @Override
    public void setState(State state) {
        super.setState(state);

        this.syncState();
        this.sync();
    }

    @Override
    public void next() {
        super.next();

        this.syncState();
        this.sync();
    }

    @Override
    public void setLocked(boolean locked) {
        super.setLocked(locked);
        this.sync();
    }

    private void syncState() {
        this.syncState(this.getInteriorDoorPosition().getBlockEntity());
        this.syncState(this.getExteriorDoorPosition().getBlockEntity());
    }

    private void syncState(Object object) {
        if (object instanceof Linkable linkable) {
            linkable.setDoor(this);
        }
    }

    private void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this);
    }
}
