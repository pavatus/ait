package loqor.ait.tardis.control.impl;

import io.wispforest.owo.ops.WorldOps;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public class AntiGravsControl extends Control {
	public AntiGravsControl() {
		super("antigravs");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}
		PropertiesHandler.set(tardis, PropertiesHandler.ANTIGRAVS_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));
		if(tardis.getExterior().getExteriorPos() != null) {
			WorldOps.updateIfOnServer(world, tardis.getExterior().getExteriorPos());
		}
		//messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));
		return true;
	}

	@Override
	public SoundEvent getSound() {
		return AITSounds.HANDBRAKE_LEVER_PULL;
	}

	public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
		// fixme translatable
		Text active = Text.translatable("tardis.message.control.antigravs.active");
		Text inactive = Text.translatable("tardis.message.control.antigravs.inactive");
		player.sendMessage((autopilot ? active : inactive), true);
	}
}
