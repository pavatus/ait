package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;

public class LandTypeControl extends Control {
    public LandTypeControl() {
        super("land_type");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return false;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        TardisTravel travel = tardis.getTravel();

        // fixme ?? im not entirely sure how to do this but it's really just my brain being fried - Loqor
        // todo use property handler to store whether we're searching upwards or downwards

        return false;
    }
}
