package mdteam.ait.tardis.control.impl.waypoint;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.WaypointHandler;
import mdteam.ait.tardis.util.Waypoint;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class LoadWaypointControl extends Control {
    public LoadWaypointControl() {
        super("load_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        WaypointHandler waypoints = tardis.getHandlers().getWaypoints();

        waypoints.setDestination();
        waypoints.spawnItem();

        return true;
    }
}
