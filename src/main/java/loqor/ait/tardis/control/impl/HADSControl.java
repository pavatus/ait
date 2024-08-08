package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.ServerAlarmHandler;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class HADSControl extends Control {

	// @TODO fix hads but for now it's changed to the alarm toggle
	public HADSControl() {
		super("alarms");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		ServerAlarmHandler alarms = tardis.getHandlers().get(TardisComponent.Id.ALARMS);
		alarms.toggle();

		return true;
	}

	@Override
	public boolean shouldFailOnNoPower() {
		return false; // todo remember to change this back when this becomes a HADS control again!!
	}
}
