package mdteam.ait.tardis.control.impl;

import mdteam.ait.AITMod;
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

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if (tardis != null)
            AITMod.openScreen(player, 0, tardis.getUuid());
        return true;
    }
}
