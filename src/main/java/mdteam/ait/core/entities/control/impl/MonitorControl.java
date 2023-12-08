package mdteam.ait.core.entities.control.impl;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import mdteam.ait.client.AITModClient;
import mdteam.ait.client.screens.MonitorScreen;
import mdteam.ait.core.entities.control.Control;
import mdteam.ait.core.helper.TardisUtil;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.impl.networking.v0.OldNetworkingHooks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.text.Text;

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
