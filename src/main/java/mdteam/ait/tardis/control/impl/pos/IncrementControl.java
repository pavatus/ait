package mdteam.ait.tardis.control.impl.pos;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

public class IncrementControl extends Control {
    public IncrementControl() {
        super("increment");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick) {
        TardisTravel travel = tardis.getTravel();

        if (!leftClick) {
            IncrementManager.nextIncrement(tardis);
        } else {
            IncrementManager.prevIncrement(tardis);
        }

        messagePlayerIncrement(player, tardis);

        return true;
    }


    @Override
    public SoundEvent getSound() {
        return AITSounds.CRANK;
    }

    private void messagePlayerIncrement(ServerPlayerEntity player, Tardis tardis) {
        Text text = Text.translatable("tardis.message.control.increment.info").append(Text.literal("" + IncrementManager.increment(tardis)));
        player.sendMessage(text, true);
    }
    @Override
    public boolean shouldHaveDelay() {
        return false;
    }
}
