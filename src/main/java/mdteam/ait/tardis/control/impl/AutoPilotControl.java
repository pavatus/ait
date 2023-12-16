package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class AutoPilotControl extends Control {
    public AutoPilotControl() {
        super("auto_pilot");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.setAutoPilot(tardis.getProperties(), !PropertiesHandler.willAutoPilot(tardis.getProperties()));

        messagePlayer(player, PropertiesHandler.willAutoPilot(tardis.getProperties()));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
        // fixme translatable
        player.sendMessage(Text.literal("Auto Pilot: " + (autopilot ? "ACTIVE" : "INACTIVE")), true);
    }
}
