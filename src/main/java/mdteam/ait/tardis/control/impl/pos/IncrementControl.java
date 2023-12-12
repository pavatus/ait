package mdteam.ait.tardis.control.impl.pos;

import mdteam.ait.tardis.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

public class IncrementControl extends Control {
    public IncrementControl() {
        super("increment");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
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

    private void messagePlayerIncrement(ServerPlayerEntity player, PosManager manager) {
        player.sendMessage(Text.literal("Increment: " + manager.increment), true); // fixme translatable is preferred
    }
}
