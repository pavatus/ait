package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class ShowSiegeModeLeverControl extends Control {

	public ShowSiegeModeLeverControl() {
		super("toggle_siege_mode_lever");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		return true;
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
	}
}