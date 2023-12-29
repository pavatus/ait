package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class ThrottleControl extends Control {
    public ThrottleControl() {
        super("throttle");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        if(tardis.isInDanger())
            return false;

        TardisTravel travel = tardis.getTravel();

        if (travel.getState() == TardisTravel.State.LANDED) {
            // fixme move all these things about out of fuel etc into the demat method, look at what we do for when the handbrake is on

            if (tardis.getFuel() <= 0) {
                player.sendMessage(Text.literal("The TARDIS is out of fuel and can not dematerialize"));
                return false;
            }
            if (tardis.isRefueling()) {
                player.sendMessage(Text.literal("The TARDIS can not dematerialize when refueling"));
                return false;
            }
            travel.dematerialise(PropertiesHandler.willAutoPilot(tardis.getHandlers().getProperties()));
        } else if (travel.getState() == TardisTravel.State.FLIGHT) {
            travel.materialise();
        }

        System.out.println(travel.getState());

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.DEMAT_LEVER_PULL;
    }
}
