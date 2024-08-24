package loqor.ait.tardis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.LinkableItem;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class NetworkUtil {

    public static <T> void send(ServerPlayerEntity player, PacketByteBuf buf, Identifier id, Codec<T> codec, T t) {
        DataResult<NbtElement> result = codec.encodeStart(NbtOps.INSTANCE, t);
        NbtElement nbt = result.resultOrPartial(AITMod.LOGGER::error).orElseThrow();

        buf.writeNbt((NbtCompound) nbt);
        send(player, id, buf);
    }

    public static void send(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        if (player == null)
            return;

        ServerPlayNetworking.send(player, id, buf);
    }

    public static <T> T receive(Codec<T> codec, PacketByteBuf buf) {
        return codec.decode(NbtOps.INSTANCE, buf.readNbt())
                .resultOrPartial(AITMod.LOGGER::error)
                .orElseThrow().getFirst();
    }

    public static void sendToInterior(ServerTardis tardis, Identifier id, PacketByteBuf buf) {
        for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
            send(player, id, buf);
        }
    }

    public static Collection<ServerPlayerEntity> getPlayersNearExterior(ServerTardis tardis) {
        if (tardis.travel().position() == null)
            return new ArrayList<>();

        return getTracking(tardis.travel().position());
    }

    /**
     * Gets players who have a linked item in their inventory
     */
    public static Collection<ServerPlayerEntity> getLinkedPlayers(ServerTardis tardis) {
        List<ServerPlayerEntity> players = new ArrayList<>();

        for (ServerPlayerEntity player : ServerLifecycleHooks.get().getPlayerManager().getPlayerList()) {
            if (hasLinkedItem(tardis, player)) {
                players.add(player);
            }
        }

        return players;
    }

    /**
     * TODO - this causes weird issues with the stacking, temporarily removed
     * Returns whether the player has an item linked to this tardis in their
     * inventory
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

    public static Collection<ServerPlayerEntity> getTracking(DirectedGlobalPos.Cached globalPos) {
        return PlayerLookup.tracking(globalPos.getWorld(), globalPos.getPos());
    }

    public static Stream<ServerPlayerEntity> getSubscribedPlayers(ServerTardis tardis) {
        Stream<ServerPlayerEntity> result = TardisUtil.getPlayersInsideInterior(tardis).stream();

        DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();

        if (exteriorPos == null || exteriorPos.getWorld() == null)
            return result;

        ChunkPos chunkPos = new ChunkPos(exteriorPos.getPos());
        return Stream.concat(result, PlayerLookup.tracking(exteriorPos.getWorld(), chunkPos).stream());
    }
}
