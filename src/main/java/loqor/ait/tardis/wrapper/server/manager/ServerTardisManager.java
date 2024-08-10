package loqor.ait.tardis.wrapper.server.manager;

import loqor.ait.api.WorldWithTardis;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.mixin.networking.ServerChunkManagerAccessor;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.old.CompliantServerTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;

import java.util.*;

public class ServerTardisManager extends CompliantServerTardisManager {

    private static ServerTardisManager instance;

    private final Set<ServerTardis> needsUpdate = new HashSet<>();

    public static void init() {
        instance = new ServerTardisManager();
    }

    private ServerTardisManager() {
        TardisEvents.SYNC_TARDIS.register(WorldWithTardis.forSync((player, tardisSet) -> {
            for (ServerTardis tardis : tardisSet) {
                if (tardis.isRemoved())
                    continue;

                this.sendTardis(player, tardis);
            }
        }));

        TardisEvents.UNLOAD_TARDIS.register(WorldWithTardis.forDesync((player, tardisSet) -> {
            for (ServerTardis tardis : tardisSet) {
                if (tardis.isRemoved())
                    continue;

                this.sendTardisRemoval(player, tardis);
            }
        }));

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerTardis tardis : this.needsUpdate) {
                if (tardis.isRemoved())
                    continue;

                DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();

                if (exteriorPos == null)
                    continue;

                ChunkPos chunkPos = new ChunkPos(exteriorPos.getPos());
                ServerChunkManager chunkManager = exteriorPos.getWorld().getChunkManager();

                ThreadedAnvilChunkStorage storage = ((ServerChunkManagerAccessor) chunkManager)
                        .getThreadedAnvilChunkStorage();

                for (ServerPlayerEntity watching : storage.getPlayersWatchingChunk(chunkPos)) {
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
        if (tardis == null || this.networkGson == null)
            return;

        if (this.fileManager.isLocked())
            return;

        this.needsUpdate.add(tardis);
    }

    public void mark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$lookup().put(chunk, tardis);
    }

    public void unmark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$withLookup(lookup -> lookup.remove(chunk, tardis));
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }
}
