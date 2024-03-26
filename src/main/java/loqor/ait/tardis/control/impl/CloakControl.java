package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.CloakData;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class CloakControl extends Control {

	public CloakControl() {
		// ⬚ ?
		super("protocol_3");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

		CloakData cloak = tardis.getHandlers().getCloak();

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_SCULK_SENSOR_CLICKING, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return false;
			}
		}

		cloak.toggle();
		// @TODO: Add translations
		if (cloak.isEnabled()) {
			// player.sendMessage(Text.translatable("tardis.cloak.on"), true);
			world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_SCULK_SENSOR_CLICKING, SoundCategory.BLOCKS, 1.0F, 1.0F);
		} else {
			// player.sendMessage(Text.literal("tardis.cloak.off"), true);
			world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_SCULK_SENSOR_CLICKING_STOP, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}

		return true;
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.INTENTIONALLY_EMPTY;
	}
}