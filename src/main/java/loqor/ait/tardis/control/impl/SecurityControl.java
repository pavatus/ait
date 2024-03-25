package loqor.ait.tardis.control.impl;

import loqor.ait.core.AITSounds;
import loqor.ait.core.item.KeyItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class SecurityControl extends Control {
	public static final String SECURITY_KEY = "security";

	public SecurityControl() {
		// ⨷ ?
		super("protocol_19");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}

		if (!hasMatchingKey(player, tardis)) {
			return false;
		}

		boolean security = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), SECURITY_KEY);

		PropertiesHandler.set(tardis, SECURITY_KEY, !security);

		security = !security;

		return true;
	}

	public static void runSecurityProtocols(Tardis tardis) {
		boolean security = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), SECURITY_KEY);
		boolean leaveBehind = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.LEAVE_BEHIND);

		if (!security) return;

		List<ServerPlayerEntity> forRemoval = new ArrayList<>();

		if (leaveBehind) {
			for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
				if (!hasMatchingKey(player, tardis)) {
					forRemoval.add(player);
				}
			}


			for (ServerPlayerEntity player : forRemoval) {
				TardisUtil.teleportOutside(tardis, player);
			}
		}
	}


	public static boolean hasMatchingKey(ServerPlayerEntity player, Tardis tardis) {
		if (player.hasPermissionLevel(2)) return true;
		if (!KeyItem.isKeyInInventory(player)) return false;
		ItemStack[] keys = KeyItem.getKeysInInventory(player);

		for (ItemStack stack : keys) {
			if (stack == null) continue;
			Tardis found = KeyItem.getTardis(stack);
			if (found == null) continue;
			if (found == tardis) return true;
		}
		return false;
	}

	@Override
	public SoundEvent getSound() {
		return AITSounds.BWEEP;
	}

	@Override
	public long getDelayLength() {
		return (long) (2.5 * 1000L);
	}
}