package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.text.Text;

public class LandTypeControl extends Control {
    public LandTypeControl() {
        super("land_type");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.setBool(tardis.getProperties(), PropertiesHandler.SEARCH_DOWN, !PropertiesHandler.getBool(tardis.getProperties(), PropertiesHandler.SEARCH_DOWN));

        messagePlayer(player, PropertiesHandler.getBool(tardis.getProperties(), PropertiesHandler.SEARCH_DOWN));

        return false;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        // fixme translatable
        String s = var ? "DOWN" : "UP";

        player.sendMessage(Text.literal("Searching: " + s), true);
    }
}
