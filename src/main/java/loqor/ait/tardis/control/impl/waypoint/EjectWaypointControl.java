package loqor.ait.tardis.control.impl.waypoint;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
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
