package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
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

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}

		PropertiesHandler.set(tardis, PropertiesHandler.ALARM_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED));

		Text alarm_enabled = Text.translatable("tardis.message.control.hads.alarm_enabled");
		Text alarms_disabled = Text.translatable("tardis.message.control.hads.alarms_disabled");
		player.sendMessage(((PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED)) ? alarm_enabled : alarms_disabled), true);

		return true;
	}

	@Override
	public boolean shouldFailOnNoPower() {
		return false; // todo remember to change this back when this becomes a HADS control again!!
	}
}
