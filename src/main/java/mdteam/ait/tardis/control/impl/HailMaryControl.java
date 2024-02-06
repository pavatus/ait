package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class HailMaryControl extends Control {
    public HailMaryControl() {
        // ♡ ?
        super("protocol_813");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.set(tardis, PropertiesHandler.HAIL_MARY, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY));
        tardis.removeFuel(50);
        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
        // fixme translatable
        Text active = Text.translatable("tardis.message.control.protocol_813.active");
        Text inactive = Text.translatable("tardis.message.control.protocol_813.inactive");
        player.sendMessage((autopilot? active : inactive), true);
    }
}
