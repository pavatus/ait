package loqor.ait.tardis.wrapper.server.manager;

import java.util.Collection;
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

import loqor.ait.AITMod;
import loqor.ait.api.WorldWithTardis;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.registry.impl.TardisComponentRegistry;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.manager.TardisBuilder;
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
            if (this.fileManager.isLocked())
                return;

            if (AITMod.AIT_CONFIG.SEND_BULK() && tardisSet.size() >= 8) {
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

    @Override
    public ServerTardis create(TardisBuilder builder) {
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
        //AITMod.LOGGER.info("writing id from {}", component);
        //AITMod.LOGGER.info("\tfor {}", rawId);

        buf.writeString(rawId);
        buf.writeString(this.networkGson.toJson(component));
    }

    private PacketByteBuf prepareSend(ServerTardis tardis) {
        PacketByteBuf data = PacketByteBufs.create();
        this.writeSend(tardis, data);

        return data;
    }

    private PacketByteBuf prepareSendDelta(ServerTardis tardis, Collection<TardisComponent> delta) {
        PacketByteBuf data = PacketByteBufs.create();

        data.writeUuid(tardis.getUuid());
        data.writeShort(delta.size());

        for (TardisComponent component : delta) {
            this.writeComponent(component, data);
        }

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

    private static boolean isInvalid(ServerTardis tardis) {
        return tardis == null || tardis.isRemoved();
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }
}
