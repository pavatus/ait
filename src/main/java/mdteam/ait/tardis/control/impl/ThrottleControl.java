package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.PropertiesHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

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
        TardisTravel travel = tardis.getTravel();

        if (travel.getState() == TardisTravel.State.LANDED) {
            travel.dematerialise(PropertiesHandler.willAutoLand(tardis.getProperties()));
        } else if (travel.getState() == TardisTravel.State.FLIGHT) {
            travel.materialise();
        }

        return true;
    }
}
