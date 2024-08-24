package loqor.ait.tardis.wrapper.server.manager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import loqor.ait.AITMod;
import loqor.ait.api.WorldWithTardis;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.util.NetworkUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.old.DeprecatedServerTardisManager;

public class ServerTardisManager extends DeprecatedServerTardisManager {

    private static ServerTardisManager instance;

    private final Set<ServerTardis> delta = new HashSet<>();

    public static void init() {
        instance = new ServerTardisManager();
    }

    private ServerTardisManager() {
        TardisEvents.SYNC_TARDIS.register(WorldWithTardis.forSync((player, tardisSet) -> {
            if (AITMod.AIT_CONFIG.SEND_BULK() && tardisSet.size() >= 8) {
                this.sendTardisBulk(player, tardisSet);
                return;
            }

            this.sendTardisAll(player, tardisSet, true);
        }));

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
            for (ServerTardis tardis : this.delta) {
                if (isInvalid(tardis))
                    continue;

                Collection<TardisComponent> delta = tardis.getDelta();

                if (delta.isEmpty())
                    return;

                PacketByteBuf buf = this.prepareSendDelta(tardis, delta);

                NetworkUtil.getSubscribedPlayers(tardis).forEach(
                        watching -> this.sendComponents(watching, buf)
                );

                tardis.clearDelta();
            }

            this.delta.clear();
        });
    }

    private void sendTardis(ServerPlayerEntity player, PacketByteBuf data) {
        ServerPlayNetworking.send(player, SEND, data);
    }

    private void sendComponents(ServerPlayerEntity player, PacketByteBuf data) {
        ServerPlayNetworking.send(player, SEND_COMPONENT, data);
    }

    private PacketByteBuf prepareSend(ServerTardis tardis) {
        PacketByteBuf data = PacketByteBufs.create();

        data.writeUuid(tardis.getUuid());
        data.writeString(this.networkGson.toJson(tardis, ServerTardis.class));

        return data;
    }

    private PacketByteBuf prepareSendDelta(ServerTardis tardis, Collection<TardisComponent> delta) {
        PacketByteBuf data = PacketByteBufs.create();

        data.writeUuid(tardis.getUuid());
        data.writeShort(delta.size());

        for (TardisComponent component : delta) {
            data.writeString(this.networkGson.toJson(component, TardisComponent.class));
        }

        return data;
    }

    protected void sendTardisBulk(ServerPlayerEntity player, Set<ServerTardis> set) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(set.size());

        for (ServerTardis tardis : set) {
            if (isInvalid(tardis))
                continue;

            data.writeUuid(tardis.getUuid());
            data.writeString(this.networkGson.toJson(tardis, ServerTardis.class));
        }

        ServerPlayNetworking.send(player, SEND_BULK, data);
    }

    protected void sendTardisAll(ServerPlayerEntity player, Set<ServerTardis> set, boolean fireEvent) {
        for (ServerTardis tardis : set) {
            if (isInvalid(tardis))
                continue;

            if (fireEvent)
                TardisEvents.SEND_TARDIS.invoker().send(tardis, player);

            this.sendTardis(player, this.prepareSend(tardis));
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

        if (isInvalid(component))
            return;

        ((ServerTardis) component.tardis()).markDirty(component);
    }

    @Override
    public void markPropertyDirty(ServerTardis tardis, Value<?> value) {
        this.markComponentDirty(value.getHolder());
    }

    private static boolean isInvalid(ServerTardis tardis) {
        return tardis == null || tardis.isRemoved();
    }

    private static boolean isInvalid(TardisComponent component) {
        return !(component.tardis() instanceof ServerTardis serverTardis) || isInvalid(serverTardis);
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }
}
