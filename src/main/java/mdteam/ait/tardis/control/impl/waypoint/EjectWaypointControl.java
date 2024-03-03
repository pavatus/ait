package mdteam.ait.tardis.control.impl.waypoint;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class EjectWaypointControl extends Control {
	public EjectWaypointControl() {
		super("eject_waypoint");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
		tardis.getHandlers().getWaypoints().spawnItem();

		return super.runServer(tardis, player, world);
	}
}
