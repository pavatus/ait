package loqor.ait.tardis.control.impl.waypoint;

import loqor.ait.core.data.Waypoint;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.WaypointHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class MarkWaypointControl extends Control {
	public MarkWaypointControl() {
		super("mark_waypoint");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		WaypointHandler handler = tardis.waypoint();

		handler.set(Waypoint.fromPos(tardis.travel().position()), console, false);
		handler.spawnItem(console);

		return true;
	}
}
