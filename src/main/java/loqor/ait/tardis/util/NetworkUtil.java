package loqor.ait.tardis.util;

import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.LinkableItem;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NetworkUtil {

	public static void send(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
		if (player == null) return;
		ServerPlayNetworking.send(player, id, buf);
	}

	/**
	 * This method syncs to the players in the tardis' interior and in the tardis' exterior and if they have a linked tardis item
	 */
	public static Collection<ServerPlayerEntity> getNearbyTardisPlayers(Tardis tardis) {
		Collection<ServerPlayerEntity> found = getPlayersInInterior(tardis);
		found.addAll(getPlayersNearExterior(tardis));
		found.addAll(getLinkedPlayers(tardis)); // todo fix issues - remove if they come back
		return found;
	}

	public static void sendToInterior(Tardis tardis, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
			send(player, id, buf);
		}
	}

	public static Collection<ServerPlayerEntity> getPlayersInInterior(Tardis tardis) {
		return TardisUtil.getPlayersInInterior(tardis);
	}

	public static Collection<ServerPlayerEntity> getPlayersNearExterior(Tardis tardis) {
		return getTracking(tardis.getExterior().getExteriorPos());
	}

	/**
	 * Gets players who have a linked item in their inventory
	 */
	public static Collection<ServerPlayerEntity> getLinkedPlayers(Tardis tardis) {
		List<ServerPlayerEntity> players = new ArrayList<>();

		for (ServerPlayerEntity player : TardisUtil.getPlayerLookup(PlayerLookup::all)) {
			if (hasLinkedItem(tardis, player)) {
				players.add(player);
			}
		}

		return players;
	}

	/**
	 * TODO - this causes weird issues with the stacking, temporarily removed
	 * Returns whether the player has an item linked to this tardis in their inventory
	 */
	public static boolean hasLinkedItem(Tardis tardis, ServerPlayerEntity player) {
		for (ItemStack stack : player.getInventory().main) {
			if (stack.isEmpty())
				continue;

			if (!(stack.getItem() instanceof LinkableItem))
				continue;

			if (!LinkableItem.isOf(player.getWorld(), stack, tardis))
				continue;

			return true;
		}

		return false;
	}

	public static Collection<ServerPlayerEntity> getTracking(AbsoluteBlockPos target) {
		return PlayerLookup.tracking((ServerWorld) target.getWorld(), target);
	}
}
