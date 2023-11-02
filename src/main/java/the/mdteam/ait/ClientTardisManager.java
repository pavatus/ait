package the.mdteam.ait;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientTardisManager extends TardisManager {

    public static final Identifier ASK = new Identifier("ait", "ask_tardis");
    private static final ClientTardisManager instance = new ClientTardisManager();

    private final Multimap<UUID, Consumer<Tardis>> subscribers = ArrayListMultimap.create();
    private final Deque<PacketByteBuf> buffers = new ArrayDeque<>();

    public ClientTardisManager() {
        ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.SEND,
                (client, handler, buf, responseSender) -> this.sync(buf)
        );

        ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.UPDATE,
                (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();

            if (!this.lookup.containsKey(uuid))
                return;

            this.sync(uuid, buf);
        });

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            for (int i = 0; i < this.buffers.size(); i++) {
                ClientPlayNetworking.send(ASK, this.buffers.pop());
            }
        });
    }

    @Override
    public void getTardis(UUID uuid, Consumer<Tardis> consumer) {
        System.out.println("CLIENT: getting tardis with uuid " + uuid);
        super.getTardis(uuid, consumer);
    }

    @Override
    public void loadTardis(UUID uuid, Consumer<Tardis> consumer) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);

        this.subscribers.put(uuid, consumer);
        this.buffers.add(data);
    }

    private void sync(UUID uuid, String json) {
        Tardis tardis = this.gson.fromJson(json, Tardis.class);

        this.lookup.put(uuid, tardis);
        for (Consumer<Tardis> consumer : this.subscribers.removeAll(uuid)) {
            consumer.accept(tardis);
        }
    }

    private void sync(UUID uuid, PacketByteBuf buf) {
        this.sync(uuid, buf.readString());
    }

    private void sync(PacketByteBuf buf) {
        this.sync(buf.readUuid(), buf);
    }

    public static ClientTardisManager getInstance() {
        return instance;
    }
}
