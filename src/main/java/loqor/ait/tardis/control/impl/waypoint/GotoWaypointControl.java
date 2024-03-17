package loqor.ait.tardis.control.impl.waypoint;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class GotoWaypointControl extends Control {
	public GotoWaypointControl() {
		super("goto_waypoint");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
		tardis.getHandlers().getWaypoints().gotoWaypoint();
		return true;
	}
}
