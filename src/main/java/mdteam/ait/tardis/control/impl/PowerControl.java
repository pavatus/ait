package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

public class PowerControl extends Control {

    private boolean noDelay = false;
    public PowerControl() {
        super("power");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if(tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
            if(tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
                this.addToControlSequence(tardis);
                this.noDelay = true;
                return false;
            }
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
        if(this.noDelay) return 0L;
        return 10000L;
    }
}
