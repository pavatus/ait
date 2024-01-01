package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class HADSControl extends Control {

    // @TODO fix hads but for now it's changed to the alarm toggle
    public HADSControl() {
        super(/*"protocol_81419"*/"alarms");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        /*PropertiesHandler.setBool(tardis.getHandlers().getProperties(),
                PropertiesHandler.HADS_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HADS_ENABLED));

        tardis.markDirty();

        player.sendMessage(Text.literal("Protocol 81419: " + (PropertiesHandler.getBool(tardis.getHandlers().getProperties(),
                        PropertiesHandler.HADS_ENABLED) ? "ENABLED" : "DISABLED")), true);

        return true;*/
        PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED));

        tardis.markDirty();
        Text alarm_enabled = Text.translatable("tardis.message.control.hads.alarm_enabled");
        Text alarms_disabled = Text.translatable("tardis.message.control.hads.alarms_disabled");
        player.sendMessage(((PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED)) ? alarm_enabled : alarms_disabled), true);

        return true;
    }
}
