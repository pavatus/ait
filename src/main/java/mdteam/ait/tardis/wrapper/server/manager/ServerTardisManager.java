package mdteam.ait.tardis.wrapper.server.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.AbstractTardisComponent;
import mdteam.ait.tardis.ITardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.manager.TardisManager;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTardisManager extends TardisManager<ServerTardis> {

    private static ServerTardisManager instance;

    private final Multimap<UUID, ServerPlayerEntity> subscribers = ArrayListMultimap.create();

    public ServerTardisManager() {
        this.loadTardises();

        ServerPlayNetworking.registerGlobalReceiver(
                ASK, (server, player, handler, buf, responseSender) -> {
                    UUID uuid = buf.readUuid();
                    this.sendTardis(player, uuid);

                    this.subscribers.put(uuid, player);
                }
        );

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> this.reset());
    }

    public ServerTardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exteriorType, TardisDesktopSchema schema) {
        UUID uuid = UUID.randomUUID();

        ServerTardis tardis = new ServerTardis(uuid, pos, schema, exteriorType);
        this.lookup.put(uuid, tardis);

        return tardis;
    }

    public ITardis getTardis(UUID uuid) {
        if (this.lookup.containsKey(uuid))
            return this.lookup.get(uuid);

        return this.loadTardis(uuid);
    }

    @Override
    public void loadTardis(UUID uuid, Consumer<ServerTardis> consumer) {
        consumer.accept(this.loadTardis(uuid));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private ServerTardis loadTardis(UUID uuid) {
        File file = ServerTardisManager.getSavePath(uuid);
        file.getParentFile().mkdirs();

        try {
            if (!file.exists())
                throw new IOException("Tardis file " + file + " doesn't exist!");

            String json = Files.readString(file.toPath());

            ServerTardis tardis = this.gson.fromJson(json, ServerTardis.class);
            tardis.init(true);

            this.lookup.put(tardis.getUuid(), tardis);
            return tardis;
        } catch (IOException e) {
            AITMod.LOGGER.warn("Failed to load tardis with uuid {}!", file);
            AITMod.LOGGER.warn(e.getMessage());
        }

        return null;
    }

    public void saveTardis() {
        for (ServerTardis tardis : this.lookup.values()) {
            this.saveTardis(tardis);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveTardis(ServerTardis tardis) {
        File savePath = ServerTardisManager.getSavePath(tardis);
        savePath.getParentFile().mkdirs();

        try {
            Files.writeString(savePath.toPath(), this.gson.toJson(tardis, ServerTardis.class));
        } catch (IOException e) {
            AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
            AITMod.LOGGER.warn(e.getMessage());
        }
    }

    public void sendToSubscribers(ITardis tardis) {
        for (ServerPlayerEntity player : this.subscribers.get(tardis.getUuid())) {
            this.sendTardis(player, tardis);
        }
    }

    public void sendToSubscribers(AbstractTardisComponent component) {
        UUID uuid = component.getTardis().getUuid();

        for (ServerPlayerEntity player : this.subscribers.get(uuid)) {
            this.updateTardis(player, uuid, component);
        }
    }

    private void updateTardis(ServerPlayerEntity player, UUID uuid, AbstractTardisComponent component) {
        this.updateTardis(player, uuid, component.getId(), this.gson.toJson(component));
    }

    private void updateTardis(ServerPlayerEntity player, UUID uuid, String header, String json) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);
        data.writeString(header);
        data.writeString(json);

        ServerPlayNetworking.send(player, UPDATE, data);
    }

    private void sendTardis(ServerPlayerEntity player, UUID uuid) {
        this.sendTardis(player, this.getTardis(uuid));
    }

    private void sendTardis(ServerPlayerEntity player, ITardis tardis) {
        this.sendTardis(player, tardis.getUuid(), this.gson.toJson(tardis, ServerTardis.class));
    }

    private void sendTardis(ServerPlayerEntity player, UUID uuid, String json) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);
        data.writeString(json);

        ServerPlayNetworking.send(player, SEND, data);
    }

    @Override
    public void reset() {
        this.subscribers.clear();

        this.saveTardis();
        super.reset();
    }

    private static File getSavePath(UUID uuid) {
        return new File(ServerTardisManager.getSavePath(), uuid + ".json");
    }

    private static File getSavePath(ITardis tardis) {
        return ServerTardisManager.getSavePath(tardis.getUuid());
    }

    private static File getSavePath() {
        return new File(TardisUtil.getSavePath() + "ait/");
    }

    public void loadTardises() {
        File[] saved = ServerTardisManager.getSavePath().listFiles();

        if (saved == null)
            return;

        for (String name : Stream.of(saved)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet())
        ) {
            if (!name.substring(name.lastIndexOf(".") + 1).equalsIgnoreCase("json"))
                continue;

            UUID uuid = UUID.fromString(name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".")));
            this.loadTardis(uuid);
        }
    }

    public static void init() {
        instance = new ServerTardisManager();
    }

    public static ServerTardisManager getInstance() {
        return instance;
    }
}
