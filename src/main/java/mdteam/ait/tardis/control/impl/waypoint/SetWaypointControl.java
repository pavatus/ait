package mdteam.ait.tardis.control.impl.waypoint;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.WaypointHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class SetWaypointControl extends Control {
    public SetWaypointControl() {
        super("set_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        WaypointHandler waypoints = tardis.getHandlers().getWaypoints();

        waypoints.setDestination();
        waypoints.spawnItem();

        return true;
    }
}
