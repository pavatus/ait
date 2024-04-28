package loqor.ait.tardis.util;

import loqor.ait.api.tardis.LinkableItem;
import loqor.ait.tardis.Tardis;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class NetworkUtil {
	// THESE SHOULD ONLY BE RAN ON SERVER
	public static void send(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
		if (player == null) return;
		ServerPlayNetworking.send(player, id, buf);
	}

	/**
	 * This method syncs to the players in the tardis' interior and in the tardis' exterior and if they have a linked tardis item
	 */
	public static void sendToTardisPlayers(Tardis tardis, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : getNearbyTardisPlayers(tardis)) {
			send(player, id, buf);
		}
	}

	/**
	 * @see #sendToTardisPlayers(Tardis, Identifier, PacketByteBuf)
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

	// sends to everyone near the exterior
	public static void sendToExterior(Tardis tardis, Identifier id, PacketByteBuf buf) {
		sendToTracking(tardis.getTravel().getPosition(), id, buf);
	}

	public static Collection<ServerPlayerEntity> getPlayersNearExterior(Tardis tardis) {
		return getTracking(tardis.getExterior().getExteriorPos());
	}

	public static void sendToLinked(Tardis tardis, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : getLinkedPlayers(tardis)) {
			send(player, id, buf);
		}
	}

	/**
	 * Gets players who have a linked item in their inventory
	 *
	 * @param tardis
	 * @return
	 */
	public static Collection<ServerPlayerEntity> getLinkedPlayers(Tardis tardis) {
		if (TardisUtil.getServer() == null) return List.of();

		List<ServerPlayerEntity> players = new ArrayList<>();

		for (ServerPlayerEntity player : PlayerLookup.all(TardisUtil.getServer())) {
			if (hasLinkedItem(tardis, player)) {
				players.add(player);
			}
		}

		return players;
	}

	/**
	 * TODO - this causes weird issues with the stacking, temporarily removed
	 * Returns whether the player has an item linked to this tardis in their inventory
	 *
	 * @param tardis
	 * @param player
	 * @return
	 */
	public static boolean hasLinkedItem(Tardis tardis, ServerPlayerEntity player) {
		for (ItemStack stack : player.getInventory().main) {
			if (!stack.isEmpty() && stack.getItem() instanceof LinkableItem) {
				if (Objects.equals(tardis, LinkableItem.getTardis(stack))) {
					return true;
				}
			}
		}
		return false;
	}

	public static void sendToTracking(AbsoluteBlockPos target, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) target.getWorld(), target)) {
			send(player, id, buf);
		}
	}

	public static Collection<ServerPlayerEntity> getTracking(AbsoluteBlockPos target) {
		return PlayerLookup.tracking((ServerWorld) target.getWorld(), target);
	}

	public static void sendToTracking(BlockEntity target, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : PlayerLookup.tracking(target)) {
			send(player, id, buf);
		}
	}

	public static void sendToTracking(Entity target, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : PlayerLookup.tracking(target)) {
			send(player, id, buf);
		}
	}

	public static void sendToWorld(ServerWorld target, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : PlayerLookup.world(target)) {
			send(player, id, buf);
		}
	}

	public static void sendToNearby(ServerWorld world, Vec3d target, double radius, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : PlayerLookup.around(world, target, radius)) {
			send(player, id, buf);
		}
	}

	public static void sendToNearby(ServerWorld world, Vec3i target, double radius, Identifier id, PacketByteBuf buf) {
		for (ServerPlayerEntity player : PlayerLookup.around(world, target, radius)) {
			send(player, id, buf);
		}
	}
}
