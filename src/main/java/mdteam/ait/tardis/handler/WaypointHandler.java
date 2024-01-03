package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.util.AbsoluteBlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class WaypointHandler extends TardisLink implements Iterable<AbsoluteBlockPos.Directed> { // todo eventually move into a positionhandler but thats too much work for rn
    private ArrayList<AbsoluteBlockPos.Directed> data;

    public WaypointHandler(UUID tardisId, ArrayList<AbsoluteBlockPos.Directed> waypoints) {
        super(tardisId);
        this.data = waypoints;
    }

    public WaypointHandler(UUID tardis) {
        this(tardis, new ArrayList<>());
    }

    public ArrayList<AbsoluteBlockPos.Directed> data() {
        return this.data;
    }

    public boolean contains(AbsoluteBlockPos.Directed var) {
        return this.data().contains(var);
    }

    public void add(AbsoluteBlockPos.Directed var) {
        this.data().add(var);

        getLinkedTardis().markDirty();
    }

    public void remove(AbsoluteBlockPos.Directed var) {
        if (!this.data().contains(var)) return;

        this.data().remove(var);
        getLinkedTardis().markDirty();
    }

    public void remove(int index) {
        this.data().remove(index);
        getLinkedTardis().markDirty();
    }

    public AbsoluteBlockPos.Directed get(int index) {
        return this.data().get(index);
    }

    @NotNull
    @Override
    public Iterator<AbsoluteBlockPos.Directed> iterator() {
        return this.data().iterator();
    }
}
