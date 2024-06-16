package loqor.ait.tardis.control.impl;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.Control;
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

		tardis.flight().handbrake().flatMap(handbrake -> !handbrake);

		if (tardis.isRefueling())
			tardis.setRefueling(false);

		boolean handbrake = tardis.flight().handbrake().get();

		this.soundEvent = handbrake ? AITSounds.HANDBRAKE_DOWN : AITSounds.HANDBRAKE_UP;
		TardisTravel travel = tardis.travel();

		if (handbrake && travel.getState() == TardisTravel.State.FLIGHT) {
			if (tardis.flight().autoLand().get()) {
				travel.setPositionToProgress();
				travel.forceLand();
				travel.playThudSound();
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
