package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class HailMaryControl extends Control {

	public HailMaryControl() {
		// ♡ ?
		super("protocol_813");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		PropertiesHandler.set(tardis, PropertiesHandler.HAIL_MARY, !PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.HAIL_MARY));
		tardis.removeFuel(50 * (tardis.tardisHammerAnnoyance + 1));

		return true;
	}
}
