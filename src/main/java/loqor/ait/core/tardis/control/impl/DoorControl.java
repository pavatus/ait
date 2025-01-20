package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;

public class DoorControl extends Control {

    private SoundEvent soundEvent = SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;

    public DoorControl() {
        super("door_control");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        this.soundEvent = !tardis.door().isOpen()
                ? AITSounds.DOOR_CONTROL
                : AITSounds.DOOR_CONTROLALT;

        tardis.door().interact(world, player.getBlockPos(), player);
        return true;
    }

    @Override
    public SoundEvent getSound() {
        return this.soundEvent;
    }
}
