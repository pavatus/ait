package mdteam.ait.tardis.wrapper.client.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDoor;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.manager.TardisManager;
import mdteam.ait.tardis.wrapper.client.ClientTardis;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;
import java.util.function.Consumer;

public class ClientTardisManager extends TardisManager<ClientTardis> {

    private static ClientTardisManager instance;

    private final Multimap<UUID, Consumer<ClientTardis>> subscribers = ArrayListMultimap.create();

    public ClientTardisManager() {
        ClientPlayNetworking.registerGlobalReceiver(SEND,
                (client, handler, buf, responseSender) -> this.sync(buf)
        );

        ClientPlayNetworking.registerGlobalReceiver(UPDATE,
                (client, handler, buf, responseSender) -> this.update(buf));

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
        switch (header) {
            case "desktop" -> tardis.setDesktop(this.gson.fromJson(json, TardisDesktop.class));
            case "door" -> tardis.setDoor(this.gson.fromJson(json, TardisDoor.class));
            case "exterior" -> tardis.setExterior(this.gson.fromJson(json, TardisExterior.class));
            case "travel" -> tardis.setTravel(this.gson.fromJson(json, TardisTravel.class));
        }
    }

    private void update(UUID uuid, PacketByteBuf buf) {
        if (!this.lookup.containsKey(uuid)) {
            this.getTardis(uuid, t -> {}); // force ASK
            return;
        }

        ClientTardis tardis = this.lookup.get(uuid);
        String header = buf.readString();
        String json = buf.readString();

        this.update(tardis, header, json);
    }

    private void update(PacketByteBuf buf) {
        this.update(buf.readUuid(), buf);
    }

    @Override
    public void reset() {
        this.subscribers.clear();
        super.reset();
    }

    public static void init() {
        instance = new ClientTardisManager();
    }

    public static ClientTardisManager getInstance() {
        return instance;
    }
}
