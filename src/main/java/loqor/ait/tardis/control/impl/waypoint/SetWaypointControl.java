package loqor.ait.tardis.control.impl.waypoint;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.WaypointHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class SetWaypointControl extends Control {
	public SetWaypointControl() {
		super("set_waypoint");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
		WaypointHandler waypoints = tardis.getHandlers().getWaypoints();

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}

		waypoints.setDestination();
		waypoints.spawnItem();

		return true;
	}
}
