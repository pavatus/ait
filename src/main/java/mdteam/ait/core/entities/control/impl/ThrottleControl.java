package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.entities.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;

public class ThrottleControl extends Control {
    public ThrottleControl() {
        super("throttle");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        // fixme this is not right, but its okay for temporary. also see remoteitem where this is done again
        // fixme sounds arent played in interior !!
        // trust me, this runs. just be patient LOQOR.
        TardisTravel travel = tardis.getTravel();

        if (travel.getState() == TardisTravel.State.LANDED) {
            travel.dematerialise(false);
        } else if (travel.getState() == TardisTravel.State.FLIGHT) {
            travel.materialise();
        }

        return true;
    }
}
