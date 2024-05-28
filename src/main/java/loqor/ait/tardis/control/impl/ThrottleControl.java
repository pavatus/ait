package loqor.ait.tardis.control.impl;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

public class ThrottleControl extends Control {
	public ThrottleControl() {
		super("throttle");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this))
			this.addToControlSequence(tardis, player);

		if (tardis.isInDanger())
			return false;

		TardisTravel travel = tardis.travel();

		if (!leftClick) {
			if (player.isSneaking()) {
				travel.speed().set(travel.maxSpeed());
			} else {
				travel.increaseSpeed();
			}
		} else {
			if (player.isSneaking()) {
				travel.speed().set(0);
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
