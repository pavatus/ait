package dev.amble.ait.core.tardis.util;

import java.util.*;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.amble.lib.data.CachedDirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.link.LinkableItem;
import dev.amble.ait.core.tardis.ServerTardis;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.util.network.c2s.SyncPropertyC2SPacket;

public class NetworkUtil {
    public static void init() {

        ServerPlayNetworking.registerGlobalReceiver(SyncPropertyC2SPacket.TYPE, SyncPropertyC2SPacket::handle);
        //ServerPlayNetworking.registerGlobalReceiver(BOTIChunkRequestC2SPacket.TYPE, BOTIChunkRequestC2SPacket::handle);

        /*if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            initClient();
        }*/
    }

    /*@Environment(EnvType.CLIENT)
    private static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(BOTISyncS2CPacket.TYPE, BOTISyncS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(BOTIDataS2CPacket.TYPE, BOTIDataS2CPacket::handle);
    }*/

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

    public static Collection<ServerPlayerEntity> getLinkedPlayers(ServerTardis tardis) {
        List<ServerPlayerEntity> players = new ArrayList<>();

        for (ServerPlayerEntity player : ServerLifecycleHooks.get().getPlayerManager().getPlayerList()) {
            if (hasLinkedItem(tardis, player)) {
                players.add(player);
            }
        }

        return players;
    }

    public static boolean hasLinkedItem(Tardis tardis, ServerPlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isEmpty())
                continue;

            if (!(stack.getItem() instanceof LinkableItem))
                continue;

            if (!LinkableItem.isOfStatic(stack, tardis))
                continue;

            return true;
        }

        return false;
    }

    public static Set<ServerTardis> findLinkedItems(ServerPlayerEntity player) {
        Set<ServerTardis> ids = new HashSet<>();

        for (ItemStack stack : player.getInventory().main) {
            if (stack.isEmpty())
                continue;

            if (!(stack.getItem() instanceof LinkableItem item))
                continue;

            Tardis tardis = item.getTardis(player.getWorld(), stack);

            if (tardis == null)
                continue;

            ids.add(tardis.asServer());
        }

        return ids;
    }

    public static Stream<ServerPlayerEntity> getSubscribedPlayers(ServerTardis tardis) {
        Stream<ServerPlayerEntity> result = TardisUtil.getPlayersInsideInterior(tardis).stream();
        CachedDirectedGlobalPos exteriorPos = tardis.travel().position();

        if (exteriorPos == null || exteriorPos.getWorld() == null)
            return result;

        ChunkPos chunkPos = new ChunkPos(exteriorPos.getPos());
        return Stream.concat(result, PlayerLookup.tracking(exteriorPos.getWorld(), chunkPos).stream());
    }

    @Environment(EnvType.CLIENT)
    public static boolean canClientSendPackets() {
        return MinecraftClient.getInstance().getNetworkHandler() != null;
    }
}
