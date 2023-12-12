package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.PropertiesHandler;
import mdteam.ait.tardis.handler.PropertiesHolder;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.text.Text;

public class LandTypeControl extends Control {
    public LandTypeControl() {
        super("land_type");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return false;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.set(tardis.getProperties(), PropertiesHandler.SEARCH_DOWN, !PropertiesHandler.get(tardis.getProperties(), PropertiesHandler.SEARCH_DOWN));

        messagePlayer(player, PropertiesHandler.get(tardis.getProperties(), PropertiesHandler.SEARCH_DOWN));

        return false;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        // fixme translatable
        String s = var ? "DOWN" : "UP";

        player.sendMessage(Text.literal("Searching: " + s), true);
    }
}
