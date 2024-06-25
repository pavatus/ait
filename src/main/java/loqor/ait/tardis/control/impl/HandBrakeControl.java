package loqor.ait.tardis.control.impl;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.TravelHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class HandBrakeControl extends Control {

	public HandBrakeControl() {
		super("handbrake");
	}

	private SoundEvent soundEvent = AITSounds.HANDBRAKE_UP;

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		if (tardis.isInDanger())
			return false;

		tardis.travel().handbrake().flatMap(handbrake -> !handbrake);

		if (tardis.isRefueling())
			tardis.setRefueling(false);

		boolean handbrake = tardis.travel().handbrake().get();

		this.soundEvent = handbrake ? AITSounds.HANDBRAKE_DOWN : AITSounds.HANDBRAKE_UP;
		TravelHandler travel = tardis.travel();

		if (handbrake && travel.getState() == TravelHandler.State.FLIGHT) {
			if (tardis.travel().autoLand().get()) {
				travel.setPosFromProgress();
				travel.finishLanding();
			} else {
				travel.crash();
			}
		}

		return true;
	}

	@Override
	public SoundEvent getSound() {
		return this.soundEvent;
	}

	@Override
	public boolean shouldFailOnNoPower() {
		return false;
	}
}
