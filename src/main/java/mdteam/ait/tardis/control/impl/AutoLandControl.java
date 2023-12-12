package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class AutoLandControl extends Control {
    public AutoLandControl() {
        super("auto_land");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.setAutoLand(tardis.getProperties(), !PropertiesHandler.willAutoLand(tardis.getProperties()));

        messagePlayer(player,PropertiesHandler.willAutoLand(tardis.getProperties()));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autoland) {
        // fixme translatable
        player.sendMessage(Text.literal("Autoland: " + autoland), true);
    }
}
