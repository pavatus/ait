package loqor.ait.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.travel.TravelHandlerBase;

public class SiegeModeControl extends Control {

    private static final Text enabled = Text.translatable("tardis.message.control.siege.enabled");
    private static final Text disabled = Text.translatable("tardis.message.control.siege.disabled");

    public SiegeModeControl() {
        // Ⓢ ?
        super("protocol_1913");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        if (tardis.travel().isCrashing() || tardis.travel().getState() != TravelHandlerBase.State.LANDED)
            return true;

        tardis.siege().setActive(!tardis.siege().isActive());
        tardis.alarm().enabled().set(false);

        player.sendMessage((tardis.siege().isActive() ? enabled : disabled), true);
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
        return 10000L;
    }
}
