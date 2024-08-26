package loqor.ait.tardis.control.impl.waypoint;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;

public class EjectWaypointControl extends Control {

    public EjectWaypointControl() {
        super("eject_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        tardis.waypoint().spawnItem(console);
        return super.runServer(tardis, player, world, console);
    }
}
