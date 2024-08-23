package loqor.ait.tardis.wrapper.server.manager;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import loqor.ait.api.WorldWithTardis;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.old.CompliantServerTardisManager;

public class ServerTardisManager extends CompliantServerTardisManager {

    private static ServerTardisManager instance;

    private final Set<ServerTardis> needsUpdate = new HashSet<>();

    public static void init() {
        instance = new ServerTardisManager();
    }

    private ServerTardisManager() {
        TardisEvents.SYNC_TARDIS.register(WorldWithTardis.forSync((player, tardisSet) -> {
            if (tardisSet.size() > 16) {
                this.sendTardisBulk(player, tardisSet);
                return;
            }

            this.sendTardisAll(player, tardisSet, true);
        }));

        if (DEMENTIA) {
            TardisEvents.UNLOAD_TARDIS.register(WorldWithTardis.forDesync((player, tardisSet) -> {
                for (ServerTardis tardis : tardisSet) {
                    if (tardis.isRemoved())
                        continue;

                    this.sendTardisRemoval(player, tardis);
                }
            }));
        }

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerTardis tardis : this.needsUpdate) {
                if (tardis.isRemoved())
                    continue;

                DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();

                if (exteriorPos == null)
                    continue;

                ChunkPos chunkPos = new ChunkPos(exteriorPos.getPos());

                for (ServerPlayerEntity watching : PlayerLookup.tracking(exteriorPos.getWorld(), chunkPos)) {
                    this.sendTardis(watching, tardis);
                }

                for (ServerPlayerEntity inside : TardisUtil.getPlayersInsideInterior(tardis)) {
                    this.sendTardis(inside, tardis);
                }
            }

            this.needsUpdate.clear();
        });
    }

    @Override
    public void sendTardis(ServerTardis tardis) {
        if (!canSend(tardis))
            return;

        if (this.fileManager.isLocked())
            return;

        this.needsUpdate.add(tardis);
    }

    protected void sendTardisBulk(ServerPlayerEntity player, Set<ServerTardis> set) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(set.size());

        for (ServerTardis tardis : set) {
            if (!canSend(tardis))
                continue;

            data.writeUuid(tardis.getUuid());
            data.writeString(this.networkGson.toJson(tardis, ServerTardis.class));
        }

        ServerPlayNetworking.send(player, SEND_BULK, data);
    }

    protected void sendTardisAll(ServerPlayerEntity player, Set<ServerTardis> set, boolean fireEvent) {
        for (ServerTardis tardis : set) {
            if (!canSend(tardis))
                continue;

            if (fireEvent)
                TardisEvents.SEND_TARDIS.invoker().send(tardis, player);

            this.sendTardis(player, tardis);
        }
    }

    public void mark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$lookup().put(chunk, tardis);
    }

    public void unmark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$withLookup(lookup -> lookup.remove(chunk, tardis));
    }

    private static boolean canSend(ServerTardis tardis) {
        return tardis != null && !tardis.isRemoved();
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }
}
