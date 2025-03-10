package dev.amble.ait.core.tardis.manager;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.TardisComponent;
import dev.amble.ait.api.TardisEvents;
import dev.amble.ait.api.WorldWithTardis;
import dev.amble.ait.core.tardis.ServerTardis;
import dev.amble.ait.core.tardis.manager.old.DeprecatedServerTardisManager;
import dev.amble.ait.core.tardis.util.NetworkUtil;
import dev.amble.ait.data.properties.Value;
import dev.amble.ait.registry.impl.TardisComponentRegistry;

public class ServerTardisManager extends DeprecatedServerTardisManager {

    private static ServerTardisManager instance;

    private final Set<ServerTardis> delta = new HashSet<>();

    public static void init() {
        instance = new ServerTardisManager();
    }

    private ServerTardisManager() {
        TardisEvents.SYNC_TARDIS.register(WorldWithTardis.forSync((player, tardisSet) -> {
            if (this.fileManager.isLocked())
                return;

            if (AITMod.CONFIG.SERVER.SEND_BULK && tardisSet.size() >= 8) {
                this.sendTardisBulk(player, tardisSet);
                return;
            }

            this.sendTardisAll(player, tardisSet);
        }));

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server)
                -> this.sendTardisAll(handler.getPlayer(), NetworkUtil.findLinkedItems(handler.getPlayer())));

        if (DEMENTIA) {
            TardisEvents.UNLOAD_TARDIS.register(WorldWithTardis.forDesync((player, tardisSet) -> {
                for (ServerTardis tardis : tardisSet) {
                    if (isInvalid(tardis))
                        continue;

                    this.sendTardisRemoval(player, tardis);
                }
            }));
        }

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (this.fileManager.isLocked())
                return;

            for (ServerTardis tardis : new HashSet<>(this.delta)) {
                if (isInvalid(tardis))
                    continue;

                if (!tardis.hasDelta())
                    continue;

                PacketByteBuf buf = this.prepareSendDelta(tardis);
                tardis.consumeDelta(component -> this.writeComponent(component, buf));

                NetworkUtil.getSubscribedPlayers(tardis).forEach(
                        watching -> this.sendComponents(watching, buf)
                );
            }

            this.delta.clear();
        });
    }

    @Override
    public ServerTardis create(TardisBuilder builder) {
        if (this.isFull()) return null;

        ServerTardis result = super.create(builder);
        this.sendTardisAll(Set.of(result));

        return result;
    }

    private void sendTardis(ServerPlayerEntity player, PacketByteBuf data) {
        ServerPlayNetworking.send(player, SEND, data);
    }

    private void sendComponents(ServerPlayerEntity player, PacketByteBuf data) {
        ServerPlayNetworking.send(player, SEND_COMPONENT, data);
    }

    private void writeSend(ServerTardis tardis, PacketByteBuf buf) {
        buf.writeUuid(tardis.getUuid());
        buf.writeString(this.networkGson.toJson(tardis, ServerTardis.class));
    }

    private void writeComponent(TardisComponent component, PacketByteBuf buf) {
        String rawId = TardisComponentRegistry.getInstance().get(component);

        buf.writeString(rawId);
        buf.writeString(this.networkGson.toJson(component));
    }

    private PacketByteBuf prepareSend(ServerTardis tardis) {
        PacketByteBuf data = PacketByteBufs.create();
        this.writeSend(tardis, data);

        return data;
    }

    private PacketByteBuf prepareSendDelta(ServerTardis tardis) {
        PacketByteBuf data = PacketByteBufs.create();

        data.writeUuid(tardis.getUuid());
        data.writeShort(tardis.getDeltaSize());

        return data;
    }

    protected void sendTardisBulk(ServerPlayerEntity player, Set<ServerTardis> set) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(set.size());

        for (ServerTardis tardis : set) {
            if (isInvalid(tardis))
                continue;

            this.writeSend(tardis, data);
        }

        ServerPlayNetworking.send(player, SEND_BULK, data);
    }

    protected void sendTardisAll(ServerPlayerEntity player, Set<ServerTardis> set) {
        for (ServerTardis tardis : set) {
            if (isInvalid(tardis))
                continue;

            TardisEvents.SEND_TARDIS.invoker().send(tardis, player);
            this.sendTardis(player, this.prepareSend(tardis));
        }
    }

    protected void sendTardisAll(Set<ServerTardis> set) {
        for (ServerTardis tardis : set) {
            if (isInvalid(tardis))
                continue;

            PacketByteBuf buf = this.prepareSend(tardis);

            NetworkUtil.getSubscribedPlayers(tardis).forEach(
                    watching -> {
                        TardisEvents.SEND_TARDIS.invoker().send(tardis, watching);
                        this.sendTardis(watching, buf);
                    }
            );
        }
    }

    public void mark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$lookup().put(chunk, tardis);
    }

    public void unmark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$withLookup(lookup -> lookup.remove(chunk, tardis));
    }

    @Override
    public void markComponentDirty(TardisComponent component) {
        if (this.fileManager.isLocked())
            return;

        if (!(component.tardis() instanceof ServerTardis tardis))
            return;

        if (isInvalid(tardis))
            return;

        tardis.markDirty(component);
        this.delta.add(tardis);
    }

    @Override
    public void markPropertyDirty(ServerTardis tardis, Value<?> value) {
        this.markComponentDirty(value.getHolder());
    }

    public boolean isFull() {
        int max = AITMod.CONFIG.SERVER.MAX_TARDISES;
        if (max <= 0) return false;

        return this.lookup.size() >= max;
    }

    private static boolean isInvalid(ServerTardis tardis) {
        return tardis == null || tardis.isRemoved();
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }
}
