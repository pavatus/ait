package mdteam.ait.tardis.control.impl;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.ShieldData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ShieldsControl extends Control {

	private SoundEvent soundEvent = AITSounds.HANDBRAKE_LEVER_PULL;

	public ShieldsControl() {
		super("shields");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}
		if(player.isSneaking()) {
			if(PropertiesHandler.getBool(tardis.getHandlers().getProperties(), ShieldData.IS_SHIELDED))
				PropertiesHandler.set(tardis, ShieldData.IS_VISUALLY_SHIELDED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), ShieldData.IS_VISUALLY_SHIELDED));
		} else {
			PropertiesHandler.set(tardis, ShieldData.IS_SHIELDED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), ShieldData.IS_SHIELDED));
		}
		this.soundEvent = player.isSneaking() ? SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME : AITSounds.HANDBRAKE_LEVER_PULL;
		if(tardis.getExterior().getExteriorPos() != null) {
			WorldOps.updateIfOnServer(world, tardis.getExterior().getExteriorPos());
		}
		return true;
	}

	@Override
	public SoundEvent getSound() {
		return this.soundEvent;
	}
}
