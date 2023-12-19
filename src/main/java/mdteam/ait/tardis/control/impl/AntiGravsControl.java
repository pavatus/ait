package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class AntiGravsControl extends Control {
    public AntiGravsControl() {
        super("antigravs");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));

        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
        // fixme translatable
        player.sendMessage(Text.literal("Antigravs: " + (autopilot ? "ACTIVE" : "INACTIVE")), true);
    }
}
