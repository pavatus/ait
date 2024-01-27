package mdteam.ait.tardis.control.impl.pos;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
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
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        TardisTravel travel = tardis.getTravel();

        PosManager postmanPat = travel.getPosManager(); // lol posmanager shortens to posman and postman pat sounds similar fixme if ur boring

        if (!player.isSneaking()) {
            postmanPat.nextIncrement();
        } else {
            postmanPat.prevIncrement();
        }

        messagePlayerIncrement(player, postmanPat);

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.CRANK;
    }

    private void messagePlayerIncrement(ServerPlayerEntity player, PosManager manager) {
        Text text = Text.translatable("tardis.message.control.increment.info").append(Text.literal("" + manager.increment));
        player.sendMessage(text, true);
    }
    @Override
    public boolean shouldHaveDelay() {
        return false;
    }
}
