package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;

public class PowerControl extends Control {

    private boolean noDelay = false;

    public PowerControl() {
        super("power");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);

            this.noDelay = true;
            return false;
        }

        tardis.fuel().togglePower();
        return false;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.HANDBRAKE_LEVER_PULL;
    }

    @Override
    public boolean requiresPower() {
        return false;
    }

    @Override
    public long getDelayLength() {
        return this.noDelay ? 0 : 10_000;
    }

    @Override
    public boolean shouldHaveDelay(Tardis tardis) {
        if (tardis.fuel().hasPower())
            return false;

        return super.shouldHaveDelay();
    }
}
