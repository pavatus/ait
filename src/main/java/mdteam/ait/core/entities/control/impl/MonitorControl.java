package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.entities.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import the.mdteam.ait.Tardis;

public class MonitorControl extends Control {
    public MonitorControl() {
        super("monitor");
    }

    // todo as there is no monitor yet and i dont know how loqor wants it, so loqor will need to do this.
    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return false;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return false;
    }
}
