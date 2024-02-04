package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public class HandBrakeControl extends Control {
    public HandBrakeControl() {
        super("handbrake");
    }

    private SoundEvent soundEvent = AITSounds.HANDBRAKE_UP;

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        if(tardis.isInDanger())
            return false;

        boolean handbrake = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE);

        PropertiesHandler.set(tardis, PropertiesHandler.HANDBRAKE, !handbrake);
        if(tardis.isRefueling())
            tardis.setRefueling(false);

        handbrake = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE);

        this.soundEvent = handbrake ? AITSounds.HANDBRAKE_DOWN : AITSounds.HANDBRAKE_UP;

        messagePlayer(player, handbrake);

        boolean autopilot = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND);
        TardisTravel travel = tardis.getTravel();

        // if (tardis.getTravel().getState() == TardisTravel.State.DEMAT) tardis.getTravel().toFlight();
        if (handbrake && travel.getState() == TardisTravel.State.FLIGHT) {
            if (autopilot) {
                travel.setPositionToProgress();
                travel.forceLand();
                travel.playThudSound();
            } else {
                travel.crash();
            }
        }

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        Text on = Text.translatable("tardis.message.control.handbrake.on");
        Text off = Text.translatable("tardis.message.control.handbrake.off");
        player.sendMessage((var? on : off), true);
    }

    @Override
    public SoundEvent getSound() {
        return this.soundEvent;
    }

    @Override
    public boolean shouldFailOnNoPower() {
        return false;
    }
}
