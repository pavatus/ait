package mdteam.ait.tardis.wrapper.server.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.manager.TardisManager;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTardisManager extends TardisManager {

    public static final Identifier SEND = new Identifier("ait", "send_tardis");
    public static final Identifier UPDATE = new Identifier("ait", "update_tardis");
    private static final String SAVE_PATH = TardisUtil.getSavePath() + "ait/";

    private static ServerTardisManager instance;

    private final Multimap<UUID, ServerPlayerEntity> subscribers = ArrayListMultimap.create();

    public ServerTardisManager() {
        this.loadTardises();

        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.ASK, (server, player, handler, buf, responseSender) -> {
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

    public Tardis getTardis(UUID uuid) {
        if (this.lookup.containsKey(uuid))
            return this.lookup.get(uuid);

        return this.loadTardis(uuid);
    }

    @Override
    public void loadTardis(UUID uuid, Consumer<Tardis> consumer) {
        consumer.accept(this.loadTardis(uuid));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Tardis loadTardis(UUID uuid) {
        File file = ServerTardisManager.getSavePath(uuid);
        file.getParentFile().mkdirs();

        try {
            if (!file.exists())
                throw new IOException("Tardis file " + file + " doesn't exist!");

            String json = Files.readString(file.toPath());
            Tardis tardis = this.gson.fromJson(json, ServerTardis.class);
            this.lookup.put(tardis.getUuid(), tardis);

            return tardis;
        } catch (IOException e) {
            AITMod.LOGGER.warn("Failed to load tardis with uuid {}!", file);
            AITMod.LOGGER.warn(e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveTardis(Tardis tardis) {
        File savePath = ServerTardisManager.getSavePath(tardis);
        savePath.getParentFile().mkdirs();

        try {
            Files.writeString(savePath.toPath(), this.gson.toJson(tardis, ServerTardis.class));
        } catch (IOException e) {
            AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
            AITMod.LOGGER.warn(e.getMessage());
        }
    }

    public void saveTardis() {
        for (Tardis tardis : this.lookup.values()) {
            this.saveTardis(tardis);
        }
    }

    public void sendToSubscribers(Tardis tardis) {
        for (ServerPlayerEntity player : this.subscribers.get(tardis.getUuid())) {
            this.sendTardis(player, tardis);
        }
    }

    private void sendTardis(ServerPlayerEntity player, UUID uuid) {
        this.sendTardis(player, this.getTardis(uuid));
    }

    private void sendTardis(ServerPlayerEntity player, Tardis tardis) {
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
        // TODO: maybe, make WorldSavePath.AIT?
        return new File(SAVE_PATH + uuid + ".json");
    }

    private static File getSavePath(Tardis tardis) {
        return ServerTardisManager.getSavePath(tardis.getUuid());
    }

    public void loadTardises() {
        File[] saved = new File(SAVE_PATH).listFiles();

        if (saved == null)
            return;

        for (String name : Stream.of(saved)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet())) {

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
