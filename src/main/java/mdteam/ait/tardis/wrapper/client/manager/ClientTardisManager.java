package mdteam.ait.tardis.wrapper.client.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import mdteam.ait.client.sounds.ClientSoundManager;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.*;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.SerialDimension;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.wrapper.client.ClientTardis;

import java.util.*;
import java.util.function.Consumer;

public class ClientTardisManager extends TardisManager<ClientTardis> {

    public static final Identifier ASK = new Identifier("ait", "ask_tardis");
    public static final Identifier ASK_POS = new Identifier("ait", "ask_pos_tardis");
    public static final Identifier LET_KNOW_UNLOADED = new Identifier("ait", "let_know_unloaded");
    private static ClientTardisManager instance;
    public final Map<ConsoleBlockEntity, Tardis> consoleToTardis = new HashMap<>();
    public final Map<ExteriorBlockEntity, Tardis> exteriorToTardis = new HashMap<>();
    public final Map<DoorBlockEntity, Tardis> interiorDoorToTardis = new HashMap<>();
    public final List<UUID> loadedTardises = new ArrayList<>();
    private final Multimap<UUID, Consumer<ClientTardis>> subscribers = ArrayListMultimap.create();

    public ClientTardisManager() {
        ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.SEND,
                (client, handler, buf, responseSender) -> this.sync(buf)
        );

        ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.UPDATE,
                (client, handler, buf, responseSender) -> this.update(buf));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Tardis tardis : ClientTardisManager.getInstance().getLookup().values()) {
                tardis.tick(client);
            }

            ClientSoundManager.tick(client);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> this.reset());
    }

    @Override
    public void loadTardis(UUID uuid, Consumer<ClientTardis> consumer) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);

        this.subscribers.put(uuid, consumer);
        ClientPlayNetworking.send(ASK, data);
    }

    private void sync(UUID uuid, String json) {
        ClientTardis tardis = this.gson.fromJson(json, ClientTardis.class);

        synchronized (this) {
            this.lookup.put(uuid, tardis);

            for (Consumer<ClientTardis> consumer : this.subscribers.removeAll(uuid)) {
                consumer.accept(tardis);
            }
        }
    }

    private void sync(UUID uuid, PacketByteBuf buf) {
        this.sync(uuid, buf.readString());
    }

    private void sync(PacketByteBuf buf) {
        this.sync(buf.readUuid(), buf);
    }
    private void update(ClientTardis tardis, String header, String json) {
        System.out.println("Updating " + header);
        switch (header) {
            case "desktop" -> tardis.setDesktop(this.gson.fromJson(json, TardisDesktop.class));
            case "door" -> tardis.setDoor(this.gson.fromJson(json, DoorData.class));
            case "exterior" -> tardis.setExterior(this.gson.fromJson(json, TardisExterior.class));
            case "travel" -> tardis.setTravel(this.gson.fromJson(json, TardisTravel.class));
        }
    }

    private void updateProperties(ClientTardis tardis, String key, String type, String value) {
        System.out.println("Updating Properties " + key + " to " + value);
        switch (type) {
            case "string" -> PropertiesHandler.set(tardis, key, value);
            case "boolean" -> PropertiesHandler.set(tardis, key, Boolean.parseBoolean(value));
            case "int" -> PropertiesHandler.set(tardis, key, Integer.parseInt(value));
            case "double" -> PropertiesHandler.set(tardis, key, Double.parseDouble(value));
            case "float" -> PropertiesHandler.set(tardis, key, Float.parseFloat(value));
            case "identifier" -> PropertiesHandler.set(tardis, key, new Identifier(value));
        }
    }

    private void update(UUID uuid, PacketByteBuf buf) {
        if (!this.lookup.containsKey(uuid)) {
            this.getTardis(uuid, t -> {}); // force ASK
            return;
        }

        ClientTardis tardis = this.lookup.get(uuid);
        String header = buf.readString();

        if (header.equals("properties")) {
            this.updateProperties(tardis, buf.readString(), buf.readString(), buf.readString());
            return;
        }

        String json = buf.readString();

        this.update(tardis, header, json);
    }

    private void update(PacketByteBuf buf) {
        this.update(buf.readUuid(), buf);
    }

    @Override
    public GsonBuilder getGsonBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(SerialDimension.class, new SerialDimension.ClientSerializer());
        return builder;
    }

    public static void init() {
        instance = new ClientTardisManager();
    }

    // https://discord.com/channels/859856751070937098/863115541166424124/1179521521555865852
    @Deprecated
    public void ask(BlockPos pos) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeBlockPos(pos);

        ClientPlayNetworking.send(ASK_POS, data);
    }
    @Deprecated
    public void ask(UUID uuid) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);

        ClientPlayNetworking.send(ASK, data);
    }

    public void letKnowUnloaded(UUID uuid) {
        ClientPlayNetworking.send(LET_KNOW_UNLOADED, PacketByteBufs.create().writeUuid(uuid));
    }

    @Override
    public void reset() {
        this.subscribers.clear();
        super.reset();
    }

    public static ClientTardisManager getInstance() {
        return instance;
    }
}
