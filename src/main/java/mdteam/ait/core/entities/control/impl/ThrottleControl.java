package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.core.entities.control.Control;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.sound.SoundCategory;

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
            travel.dematerialise(false);
        } else if (travel.getState() == TardisTravel.State.FLIGHT) {
            travel.checkPositionAndMaterialise(true);
        }

        return true;
    }
}
