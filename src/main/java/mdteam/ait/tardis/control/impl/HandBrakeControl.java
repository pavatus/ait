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

        PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));
        if(tardis.isRefueling())
            tardis.setRefueling(false);

        tardis.markDirty();

        this.soundEvent = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE) ? AITSounds.HANDBRAKE_DOWN : AITSounds.HANDBRAKE_UP;

        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));

        // if (tardis.getTravel().getState() == TardisTravel.State.DEMAT) tardis.getTravel().toFlight();
        if (tardis.getTravel().getState() == TardisTravel.State.FLIGHT) tardis.getTravel().crash();

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
