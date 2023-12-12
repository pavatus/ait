package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.PropertiesHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.text.Text;

public class HandBrakeControl extends Control {
    public HandBrakeControl() {
        super("handbrake");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return false;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.set(tardis.getProperties(), PropertiesHandler.HANDBRAKE, !PropertiesHandler.get(tardis.getProperties(), PropertiesHandler.HANDBRAKE));

        messagePlayer(player,PropertiesHandler.get(tardis.getProperties(), PropertiesHandler.HANDBRAKE));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        // fixme translatable
        String s = var ? "ON" : "OFF";

        player.sendMessage(Text.literal("Handbrake: " + s), true);
    }
}
