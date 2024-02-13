package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

public class PowerControl extends Control {
    public PowerControl() {
        super("power");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if(tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
            this.addToControlSequence(tardis);
            if(tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) return false;
        }
        tardis.togglePower();

        return false;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.HANDBRAKE_LEVER_PULL;
    }
    @Override
    public boolean shouldFailOnNoPower() {
        return false;
    }

    @Override
    public long getDelayLength() {
        return 10000L;
    }
}
