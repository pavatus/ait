package loqor.ait.tardis.control.impl.waypoint;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.WaypointHandler;
import loqor.ait.tardis.util.Waypoint;
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
