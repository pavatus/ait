package loqor.ait.core.tardis.control.impl.waypoint;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.handler.WaypointHandler;
import loqor.ait.data.Waypoint;

public class MarkWaypointControl extends Control {
    public MarkWaypointControl() {
        super("mark_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        WaypointHandler handler = tardis.waypoint();

        handler.set(Waypoint.fromPos(tardis.travel().position()), console, true);

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.MARK_WAYPOINT;
    }
}
