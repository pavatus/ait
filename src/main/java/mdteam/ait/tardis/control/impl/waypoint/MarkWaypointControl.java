package mdteam.ait.tardis.control.impl.waypoint;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.WaypointHandler;
import mdteam.ait.tardis.util.Waypoint;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class MarkWaypointControl extends Control {
    public MarkWaypointControl() {
        super("mark_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        WaypointHandler handler = tardis.getHandlers().getWaypoints();

        handler.set(Waypoint.fromDirected(tardis.getTravel().getPosition()), false);
        handler.spawnItem();

        return true;
    }
}
