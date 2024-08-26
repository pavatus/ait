package loqor.ait.tardis.control.impl;

import io.wispforest.owo.ops.WorldOps;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.TardisComponent;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.handler.ShieldHandler;

public class ShieldsControl extends Control {

    private SoundEvent soundEvent = AITSounds.HANDBRAKE_LEVER_PULL;

    public ShieldsControl() {
        super("shields");
    }
    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        ShieldHandler shields = tardis.handler(TardisComponent.Id.SHIELDS);

        if (leftClick) {
            if (shields.shielded().get())
                shields.toggleVisuals();
        } else {
            shields.toggle();

            if (shields.visuallyShielded().get())
                shields.disableVisuals();
        }

        this.soundEvent = leftClick ? SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME : AITSounds.HANDBRAKE_LEVER_PULL;

        if (tardis.travel().position().getPos() != null)
            WorldOps.updateIfOnServer(world, tardis.travel().position().getPos());

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return this.soundEvent;
    }
}
