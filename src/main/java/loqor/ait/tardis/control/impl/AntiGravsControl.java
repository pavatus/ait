package loqor.ait.tardis.control.impl;

import io.wispforest.owo.ops.WorldOps;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class AntiGravsControl extends Control {

	public AntiGravsControl() {
		super("antigravs");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		PropertiesHandler.set(tardis, PropertiesHandler.ANTIGRAVS_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));

		if (tardis.travel().position().getPos() != null)
			WorldOps.updateIfOnServer(world, tardis.travel().position().getPos());

		return true;
	}

	@Override
	public SoundEvent getSound() {
		return AITSounds.HANDBRAKE_LEVER_PULL;
	}
}
