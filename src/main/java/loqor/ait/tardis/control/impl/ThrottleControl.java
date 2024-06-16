package loqor.ait.tardis.control.impl;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.FlightData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ThrottleControl extends Control {
	public ThrottleControl() {
		super("throttle");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this))
			this.addToControlSequence(tardis, player, console);

		if (tardis.isInDanger())
			return false;

		TardisTravel travel = tardis.travel();
		FlightData flight = tardis.flight();

		if (!leftClick) {
			if (player.isSneaking()) {
				flight.speed().set(flight.maxSpeed());
			} else {
				travel.increaseSpeed();
			}
		} else {
			if (player.isSneaking()) {
				flight.speed().set(0);
			} else {
				travel.decreaseSpeed();
			}
		}

		if(travel.getState() == TardisTravel.State.DEMAT)
			tardis.sequence().setActivePlayer(player);

		return true;
	}

	@Override
	public SoundEvent getSound() {
		return AITSounds.DEMAT_LEVER_PULL;
	}

	@Override
	public boolean shouldFailOnNoPower() {
		return false;
	}
}
