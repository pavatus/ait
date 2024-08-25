package loqor.ait.tardis.wrapper.server.manager.old;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.wispforest.owo.ops.WorldOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.api.WorldWithTardis;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.events.ServerCrashEvent;
import loqor.ait.core.events.WorldSaveEvent;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.core.util.WorldUtil;
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
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

public abstract class DeprecatedServerTardisManager extends TardisManager<ServerTardis, MinecraftServer> {

    protected final TardisFileManager<ServerTardis> fileManager = new TardisFileManager<>();

    public DeprecatedServerTardisManager() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.fileManager.setLocked(false));
        ServerLifecycleEvents.SERVER_STOPPING.register(this::saveAndReset);

        ServerCrashEvent.EVENT.register(((server, report) -> this.reset())); // just panic and reset
        WorldSaveEvent.EVENT.register(world -> this.save(world.getServer(), false));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerTardis tardis : this.lookup.values()) {
                if (tardis.isRemoved())
                    continue;

                tardis.tick(server);
            }
        });
    }

    @Override
    protected GsonBuilder createGsonBuilder(Exclude.Strategy strategy) {
        return super.createGsonBuilder(strategy)
                .registerTypeAdapter(Tardis.class, ServerTardis.creator());
    }

    public ServerTardis create(TardisBuilder builder) {
        ServerTardis tardis = builder.build();
        this.lookup.put(tardis);

        return tardis;
    }

    protected void sendTardisRemoval(MinecraftServer server, ServerTardis tardis) {
        if (tardis == null)
            return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(tardis.getUuid());

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            this.sendTardisRemoval(player, data);
        }
    }

    protected void sendTardisRemoval(ServerPlayerEntity player, ServerTardis tardis) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(tardis.getUuid());

        this.sendTardisRemoval(player, data);
    }

    protected void sendTardisRemoval(ServerPlayerEntity player, PacketByteBuf data) {
        ServerPlayNetworking.send(player, REMOVE, data);
    }

    public abstract void markComponentDirty(TardisComponent component);

    public abstract void markPropertyDirty(ServerTardis tardis, Value<?> value);

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
        return this.fileManager.loadTardis(server, this, uuid, this::readTardis, this.lookup::put);
    }

    public void remove(MinecraftServer server, ServerTardis tardis) {
        tardis.setRemoved(true);

        ServerWorld tardisWorld = WorldUtil.getTardisDimension();

        // Remove the exterior if it exists
        DirectedGlobalPos.Cached exteriorPos = tardis.travel().position();

        if (exteriorPos != null) {
            TardisUtil.getPlayersInsideInterior(tardis).forEach(player -> TardisUtil.teleportOutside(tardis, player));

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
            ((WorldWithTardis) world).ait$withLookup(HashMap::clear);
        }
    }

    private void saveAndReset(MinecraftServer server) {
        this.save(server, true);
        this.reset();
    }

    /**
     * @return An initialized {@link ServerTardis} without attachments.
     */
    protected ServerTardis readTardis(Gson gson, JsonObject json) {
        ServerTardis tardis = gson.fromJson(json, ServerTardis.class);
        Tardis.init(tardis, TardisComponent.InitContext.deserialize());

        return tardis;
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
