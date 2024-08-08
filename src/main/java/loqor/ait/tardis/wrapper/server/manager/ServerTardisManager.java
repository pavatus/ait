package loqor.ait.tardis.wrapper.server.manager;

import com.google.gson.GsonBuilder;
import io.wispforest.owo.ops.WorldOps;
import loqor.ait.api.WorldWithTardis;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.SerialDimension;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.events.ServerCrashEvent;
import loqor.ait.core.events.WorldSaveEvent;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.mixin.networking.ServerChunkManagerAccessor;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.manager.TardisBuilder;
import loqor.ait.tardis.manager.TardisFileManager;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.util.desktop.structures.DesktopGenerator;
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
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ServerTardisManager extends TardisManager<ServerTardis, MinecraftServer> {

    private static ServerTardisManager instance;
    private final TardisFileManager<ServerTardis> fileManager = new TardisFileManager<>();

    private final Set<ServerTardis> needsUpdate = new HashSet<>();

    public static void init() {
        instance = new ServerTardisManager();
    }

    private ServerTardisManager() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.fileManager.setLocked(false));
        ServerLifecycleEvents.SERVER_STOPPING.register(this::saveAndReset);

        ServerCrashEvent.EVENT.register(((server, report) -> this.reset())); // just panic and reset
        WorldSaveEvent.EVENT.register(world -> this.save(world.getServer(), false));

        TardisEvents.SYNC_TARDIS.register((player, chunk) -> {
            WorldWithTardis tardisWorld = (WorldWithTardis) chunk.getWorld();
            Set<ServerTardis> set = tardisWorld.ait$lookup().get(chunk.getPos());

            if (set == null)
                return;

            for (ServerTardis tardis : set) {
                if (tardis.isRemoved())
                    continue;

                this.sendTardis(player, tardis);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerTardis tardis : this.lookup.values()) {
                if (tardis.isRemoved())
                    continue;

                tardis.tick(server);
            }
        });

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

                List<ServerPlayerEntity> players = new ArrayList<>();

                players.addAll(storage.getPlayersWatchingChunk(chunkPos));
                players.addAll(TardisUtil.getPlayersInsideInterior(tardis));

                for (ServerPlayerEntity player : players) {
                    sendTardis(player, tardis);
                }
            }

            this.needsUpdate.clear();
        });
    }

    @Override
    protected GsonBuilder createGsonBuilder(Exclude.Strategy strategy) {
        return super.createGsonBuilder(strategy)
                .registerTypeAdapter(SerialDimension.class, SerialDimension.serializer())
                .registerTypeAdapter(Tardis.class, ServerTardis.creator());
    }

    public void sendTardis(ServerPlayerEntity player, Tardis tardis) {
        if (tardis == null)
            return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(tardis.getUuid());
        data.writeString(this.networkGson.toJson(tardis, ServerTardis.class));

        ServerPlayNetworking.send(player, SEND, data);
    }

    private void sendTardisRemoval(MinecraftServer server, Tardis tardis) {
        if (tardis == null)
            return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(tardis.getUuid());

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, REMOVE, data);
        }
    }

    public void sendTardis(ServerTardis tardis) {
        if (tardis == null || this.networkGson == null)
            return;

        if (this.fileManager.isLocked())
            return;

        this.needsUpdate.add(tardis);
    }

    public void sendTardis(TardisComponent component) {
        if (component.tardis() instanceof ServerTardis tardis)
            sendTardis(tardis); // TODO
    }

    public void sendPropertyV2ToSubscribers(Tardis tardis, Value<?> value) {
        sendTardis((ServerTardis) tardis); // TODO
    }

    public void sendToSubscribers(ServerTardis tardis) {
        sendTardis(tardis);
    }

    public ServerTardis create(TardisBuilder builder) {
        ServerTardis tardis = builder.build();
        this.lookup.put(tardis);

        return tardis;
    }

    public void remove(MinecraftServer server, ServerTardis tardis) {
        tardis.setRemoved(true);

        ServerWorld tardisWorld = (ServerWorld) TardisUtil.getTardisDimension();

        // Remove the exterior if it exists
        DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();

        if (exteriorPos != null) {
            TardisUtil.getPlayersInsideInterior(tardis).forEach(player ->
                    TardisUtil.teleportOutside(tardis, player));

            World world = exteriorPos.getWorld();
            BlockPos pos = exteriorPos.getPos();

            world.removeBlock(pos, false);
            world.removeBlockEntity(pos);
        } else {
            TardisUtil.getPlayersInsideInterior(tardis).forEach(player -> {
                DirectedGlobalPos.Cached cached = tardis.travel().destination();
                WorldOps.teleportToWorld(player, cached.getWorld(), cached.getPos().toCenterPos());
            });
        }

        this.sendTardisRemoval(server, tardis);

        // Remove the interior door
        DirectedBlockPos interiorDorPos = tardis.getDesktop().doorPos();

        if (interiorDorPos != null) {
            BlockPos interiorDoor = interiorDorPos.getPos();

            tardisWorld.removeBlock(interiorDoor, false);
            tardisWorld.removeBlockEntity(interiorDoor);
        }

        // Remove the interior
        DesktopGenerator.clearArea(tardisWorld, tardis.getDesktop().getCorners());

        this.fileManager.delete(server, tardis.getUuid());
        this.lookup.remove(tardis.getUuid());
    }

    @Override
    public @Nullable ServerTardis demandTardis(MinecraftServer server, UUID uuid) {
        if (uuid == null)
            return null; // ugh - ong bro

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

    public void mark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        ((WorldWithTardis) world).ait$lookup().put(chunk, tardis);
    }

    public void unmark(ServerWorld world, ServerTardis tardis, ChunkPos chunk) {
        WorldWithTardis withTardis = (WorldWithTardis) world;

        if (!withTardis.ait$hasTardis())
            return;

        withTardis.ait$lookup().remove(chunk, tardis);
    }

    private void save(MinecraftServer server, boolean clean) {
        if (clean)
            this.fileManager.setLocked(true);

        for (ServerTardis tardis : this.lookup.values()) {
            if (clean) {

                if (tardis == null)
                    continue;

                ForcedChunkUtil.stopForceLoading(tardis.travel().position());
                TravelHandlerBase.State state = tardis.travel().getState();

                if (state == TravelHandlerBase.State.DEMAT) {
                    tardis.travel().finishDemat();
                } else if (state == TravelHandlerBase.State.MAT) {
                    tardis.travel().finishRemat();
                }

                tardis.door().closeDoors();
            }

            this.fileManager.saveTardis(server, this, tardis);
        }

        if (!clean)
            return;

        for (ServerWorld world : server.getWorlds()) {
            WorldWithTardis withTardis = (WorldWithTardis) world;

            if (withTardis.ait$hasTardis())
                continue;

            withTardis.ait$lookup().clear();
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
