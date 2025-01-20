package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.TardisComponent;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.handler.ServerAlarmHandler;

public class HADSControl extends Control {

    // @TODO fix hads but for now it's changed to the alarm toggle
    public HADSControl() {
        super("alarms");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        ServerAlarmHandler alarms = tardis.getHandlers().get(TardisComponent.Id.ALARMS);
        alarms.toggle();

        return true;
    }

    @Override
    public boolean requiresPower() {
        return false; // todo remember to change this back when this becomes a HADS control again!!
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.ALARM;
    }
}
