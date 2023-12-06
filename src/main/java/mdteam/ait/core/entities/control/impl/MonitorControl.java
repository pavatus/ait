package mdteam.ait.core.entities.control.impl;

import mdteam.ait.client.screens.MonitorScreen;
import mdteam.ait.core.entities.control.Control;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.text.Text;

public class MonitorControl extends Control {
    public MonitorControl() {
        super("monitors");
    }

    // todo as there is no monitors yet and i dont know how loqor wants it, so loqor will need to do this.
    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        MinecraftClient.getInstance().setScreenAndRender(new MonitorScreen(Text.literal("WHAT")));
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        System.out.println("is the monitor running stuff?");
        return true;
    }
}
