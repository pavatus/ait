package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class HailMaryControl extends Control {
    public HailMaryControl() {
        super("protocol_813");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY));
        tardis.markDirty();
        tardis.removeFuel(50);
        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
        // fixme translatable
        player.sendMessage(Text.literal("Protocol 813: " + (autopilot ? "ACTIVE" : "INACTIVE")), true);
    }
}
