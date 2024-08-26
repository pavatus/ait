package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;

public class MonitorControl extends Control {

    public MonitorControl() {
        super("monitor");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
            boolean leftClick) {
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);

        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        AITMod.openScreen(player, 0, tardis.getUuid(), console);
        return true;
    }
}
