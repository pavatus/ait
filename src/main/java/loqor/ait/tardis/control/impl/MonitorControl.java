package loqor.ait.tardis.control.impl;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisConsole;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

public class MonitorControl extends Control {
	public MonitorControl() {
		super("monitor");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick, TardisConsole console) {
		player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis, player);
				return false;
			}
		}

		AITMod.openScreen(player, 0, tardis.getUuid(), console.uuid());

		return true;
	}
}
