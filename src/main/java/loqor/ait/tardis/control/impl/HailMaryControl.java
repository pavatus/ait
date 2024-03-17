package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.control.Control;
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

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}

		PropertiesHandler.set(tardis, PropertiesHandler.HAIL_MARY, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY));
		tardis.removeFuel(50 * (tardis.tardisHammerAnnoyance + 1));
		// messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY));

		return true;
	}

	public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
		// fixme translatable
		Text active = Text.translatable("tardis.message.control.protocol_813.active");
		Text inactive = Text.translatable("tardis.message.control.protocol_813.inactive");
		player.sendMessage((autopilot ? active : inactive), true);
	}
}
