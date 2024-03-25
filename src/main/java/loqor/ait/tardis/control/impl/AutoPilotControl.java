package loqor.ait.tardis.control.impl;

import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class AutoPilotControl extends Control {
	public AutoPilotControl() {
		// ☸ ?
		super("protocol_116");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}

		if(player.isSneaking() && tardis.getTravel().getState() == TardisTravel.State.LANDED) {
			if(tardis.getDoor().isOpen()) {
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CHAIN_FALL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return true;
			} else {
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			TardisUtil.teleportOutside(tardis, player);
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 1, false, false, false));
			TardisRealEntity.spawnFromTardisId(tardis.getExterior().getExteriorPos().getWorld(), tardis.getUuid(), tardis.getExterior().getExteriorPos(), player, player.getBlockPos());
			return true;
		}

		PropertiesHandler.set(tardis, PropertiesHandler.AUTO_LAND, !PropertiesHandler.willAutoPilot(tardis.getHandlers().getProperties()));

		// messagePlayer(player, PropertiesHandler.willAutoPilot(tardis.getHandlers().getProperties()));

		return true;
	}

	public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
		// fixme translatable
		Text active = Text.translatable("tardis.message.control.protocol_116.active");
		Text inactive = Text.translatable("tardis.message.control.protocol_116.inactive");
		player.sendMessage((autopilot ? active : inactive), true);
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.BLOCK_LEVER_CLICK;
	}
}
