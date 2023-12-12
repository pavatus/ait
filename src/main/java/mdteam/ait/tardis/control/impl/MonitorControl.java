package mdteam.ait.tardis.control.impl;

import mdteam.ait.client.AITModClient;
import mdteam.ait.tardis.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;

public class MonitorControl extends Control {
    public MonitorControl() {
        super("monitor");
    }

    // todo as there is no monitors yet and i dont know how loqor wants it, so loqor will need to do this.
    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        AITModClient.openScreen(player, 0, tardis.getUuid());
        return true;
    }
}
