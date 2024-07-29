package loqor.ait.tardis.wrapper.server.manager;

import com.google.gson.GsonBuilder;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.compat.immersive.PortalsHandler;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.SerialDimension;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.events.ServerCrashEvent;
import loqor.ait.core.events.WorldSaveEvent;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.mixin.networking.ChunkHolderAccessor;
import loqor.ait.mixin.networking.ServerChunkManagerAccessor;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.manager.TardisBuilder;
import loqor.ait.tardis.manager.TardisFileManager;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ServerTardisManager extends TardisManager<ServerTardis, MinecraftServer> {

    private static ServerTardisManager instance;
    private final TardisFileManager<ServerTardis> fileManager = new TardisFileManager<>();

    private final Map<ChunkPos, Set<Tardis>> tardisChunks = new HashMap<>();
    private final Set<Tardis> buffer = new HashSet<>();

    public static void init() {
        instance = new ServerTardisManager();
    }

    private ServerTardisManager() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.fileManager.setLocked(false));
        ServerLifecycleEvents.SERVER_STOPPING.register(this::saveAndReset);

        ServerCrashEvent.EVENT.register(((server, report) -> this.reset())); // just panic and reset
        WorldSaveEvent.EVENT.register(world -> this.save(world.getServer(), false));

        TardisEvents.SYNC_TARDIS.register((player, chunk) -> {
            Set<Tardis> set = this.tardisChunks.get(chunk);

            if (set == null)
                return;

            for (Tardis tardis : set) {
                AITMod.LOGGER.info("Sending tardis {} at chunk {}", tardis.getUuid(), chunk);
                this.sendTardis(player, tardis);
            }
        });

        TardisEvents.UNLOAD_TARDIS.register((player, chunk) -> {
            AITMod.LOGGER.info("Un-loading buffer at chunk {}", chunk);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerTardis tardis : this.lookup.values()) {
                tardis.tick(server);
            }
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (Tardis tardis : this.buffer) {
                long start = System.currentTimeMillis();

                DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();
                ChunkPos chunkPos = new ChunkPos(exteriorPos.getPos());

                ServerChunkManager chunkManager = exteriorPos.getWorld().getChunkManager();
                ChunkHolder holder = ((ServerChunkManagerAccessor) chunkManager).ait$chunkHolder(chunkPos.toLong());
                ThreadedAnvilChunkStorage storage = (ThreadedAnvilChunkStorage) ((ChunkHolderAccessor) holder).getPlayersWatchingChunkProvider();

                List<ServerPlayerEntity> players = new ArrayList<>();

                players.addAll(storage.getPlayersWatchingChunk(chunkPos));
                players.addAll(TardisUtil.getPlayersInsideInterior(tardis));

                for (ServerPlayerEntity player : players) {
                    sendTardis(player, tardis);
                }

                AITMod.LOGGER.info("Updating tardis took {}ms", System.currentTimeMillis() - start);
            }
            this.buffer.clear();
        });
    }

    @Override
    protected GsonBuilder createGsonBuilder(Exclude.Strategy strategy) {
        return super.createGsonBuilder(strategy)
                .registerTypeAdapter(SerialDimension.class, SerialDimension.serializer())
                .registerTypeAdapter(Tardis.class, ServerTardis.creator());
    }

    public void sendTardis(ServerPlayerEntity player, Tardis tardis) {
        if (tardis == null || this.networkGson == null)
            return;

        if (this.fileManager.isLocked()) return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(tardis.getUuid());
        data.writeString(this.networkGson.toJson(tardis, ServerTardis.class));

        ServerPlayNetworking.send(player, SEND, data);
    }

    public void sendTardis(Tardis tardis) {
        if (tardis == null || this.networkGson == null)
            return;

        if (this.fileManager.isLocked()) return;
        this.buffer.add(tardis);
    }

    public void sendTardis(TardisComponent component) {
        sendTardis(component.tardis()); // TODO
    }

    public void sendPropertyV2ToSubscribers(Tardis tardis, Value<?> value) {
        sendTardis(tardis); // TODO
    }

    public void sendPropertyToSubscribers(Tardis tardis, PropertiesHolder holder, String key, String type, String value) {
        sendTardis(tardis); // TODO
    }

    public void sendToSubscribers(ServerTardis tardis) {
        sendTardis(tardis);
    }

    public ServerTardis create(TardisBuilder builder) {
        ServerTardis tardis = builder.build();
        this.lookup.put(tardis);

        return tardis;
    }

    public void remove(MinecraftServer server, Tardis tardis) {
        this.fileManager.delete(server, tardis.getUuid());
        this.lookup.remove(tardis.getUuid());

        DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();
        ChunkPos chunkPos = new ChunkPos(exteriorPos.getPos());

        this.unmark(tardis, chunkPos);
    }

    @Override
    public @Nullable ServerTardis demandTardis(MinecraftServer server, UUID uuid) {
        if (uuid == null)
            return null; // ugh

        ServerTardis result = this.lookup.get(uuid);

        if (result == null)
            result = this.loadTardis(server, uuid);

        return result;
    }

    @Override
    public void loadTardis(MinecraftServer server, UUID uuid, @Nullable Consumer<ServerTardis> consumer) {
        if (consumer != null)
            consumer.accept(this.loadTardis(server, uuid));
    }

    private ServerTardis loadTardis(MinecraftServer server, UUID uuid) {

        return this.fileManager.loadTardis(
                server, this, uuid, this::readTardis, this.lookup::put
        );
    }

    public void mark(Tardis tardis, ChunkPos chunk) {
        AITMod.LOGGER.info("Marked tardis {} at {}", tardis, chunk);
        this.tardisChunks.computeIfAbsent(chunk,
                chunkPos -> new HashSet<>()
        ).add(tardis);
    }

    public void unmark(Tardis tardis, ChunkPos chunk) {
        AITMod.LOGGER.info("Unmarked tardis {} at {}", tardis, chunk);
        Set<Tardis> set = this.tardisChunks.get(chunk);
        if (set == null) return;
        set.remove(tardis);
    }

    private void save(MinecraftServer server, boolean lock) {
        if (lock)
            this.fileManager.setLocked(true);

        // force all dematting to go flight and all matting to go land
        for (ServerTardis tardis : this.lookup.values()) {
            // stop forcing all chunks
            if (lock) {
                ForcedChunkUtil.stopForceLoading(tardis.travel().position());
                TravelHandlerBase.State state = tardis.travel().getState();

                if (state == TravelHandlerBase.State.DEMAT) {
                    tardis.travel().finishDemat();
                } else if (state == TravelHandlerBase.State.MAT) {
                    tardis.travel().finishRemat();
                }

                tardis.door().closeDoors();

                if (DependencyChecker.hasPortals())
                    PortalsHandler.removePortals(tardis);
            }

            this.fileManager.saveTardis(server, this, tardis);
        }
    }

    private void saveAndReset(MinecraftServer server) {
        this.save(server, true);
        this.reset();
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }

    public static ServerPlayNetworking.PlayChannelHandler receiveTardis(Receiver receiver) {
        return (server, player, handler, buf, responseSender) -> {
            ServerTardisManager.getInstance().getTardis(server, buf.readUuid(),
                    tardis -> receiver.receive(tardis, server, player, handler, buf, responseSender));
        };
    }

    @FunctionalInterface
    public interface Receiver {
        void receive(ServerTardis tardis, MinecraftServer server, ServerPlayerEntity player,
                     ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender);
    }
}
