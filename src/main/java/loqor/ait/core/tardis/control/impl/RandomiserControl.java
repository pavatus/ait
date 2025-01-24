package loqor.ait.core.tardis.control.impl;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.control.impl.pos.IncrementManager;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelUtil;

public class RandomiserControl extends Control {

    public RandomiserControl() {
        super("randomiser");
    }

    /**
     * TODO rewrite the randomizer to follow the async stuff like this class
     * {@link loqor.ait.core.tardis.util.AsyncLocatorUtil}
     */
    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        TravelHandler travel = tardis.travel();

        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        TravelUtil.randomPos(tardis, 10, IncrementManager.increment(tardis), cached -> {
            tardis.travel().destination(cached);
            tardis.removeFuel(0.1d * IncrementManager.increment(tardis) * tardis.travel().instability());

            messagePlayer(player, travel);
        });

        return true;
    }

    @Override
    public long getDelayLength() {
        return 40;
    }

    private void messagePlayer(ServerPlayerEntity player, TravelHandler travel) {
        CachedDirectedGlobalPos dest = travel.destination();
        BlockPos pos = dest.getPos();

        Text text = Text.translatable("tardis.message.control.randomiser.destination")
                .append(Text.literal(pos.getX() + " | " + pos.getY() + " | " + pos.getZ()));
        player.sendMessage(text, true);
    }
    @Override
    public SoundEvent getSound() {
        return AITSounds.RANDOMIZE;
    }
}
