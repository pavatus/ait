package dev.amble.ait.client.tardis.manager;

import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.TardisComponent;
import dev.amble.ait.client.sounds.ClientSoundManager;
import dev.amble.ait.client.tardis.ClientTardis;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.TardisManager;
import dev.amble.ait.data.Exclude;
import dev.amble.ait.registry.impl.TardisComponentRegistry;

public class ClientTardisManager extends TardisManager<ClientTardis, MinecraftClient> {

    private static ClientTardisManager instance;

    private final Multimap<UUID, Consumer<ClientTardis>> subscribers = ArrayListMultimap.create();

    public static void init() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new UnsupportedOperationException("Tried to initialize ClientTardisManager on the server!");

        instance = new ClientTardisManager();
    }

    private ClientTardisManager() {
        ClientPlayNetworking.registerGlobalReceiver(SEND, (client, handler, buf, responseSender) -> this.syncTardis(buf));

        ClientPlayNetworking.registerGlobalReceiver(SEND_BULK,
                (client, handler, buf, responseSender) -> this.syncBulk(buf));

        ClientPlayNetworking.registerGlobalReceiver(REMOVE, (client, handler, buf, responseSender) -> this.remove(buf));

        ClientPlayNetworking.registerGlobalReceiver(SEND_COMPONENT, (client, handler, buf, responseSender) -> this.syncDelta(buf));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null)
                return;

            for (ClientTardis tardis : this.lookup.values()) {
                tardis.tick(client);
            }

            ClientSoundManager.tick(client);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> this.reset());
        ClientLoginConnectionEvents.DISCONNECT.register((client, reason) -> this.reset());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> this.reset());
    }

    private void remove(PacketByteBuf buf) {
        this.lookup.remove(buf.readUuid());
    }

    @Override
    public void loadTardis(MinecraftClient client, UUID uuid, @Nullable Consumer<ClientTardis> consumer) {
        if (client.player == null)
            return;

        if (uuid == null)
            return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);

        if (consumer != null)
            this.subscribers.put(uuid, consumer);

        MinecraftClient.getInstance().executeTask(() -> ClientPlayNetworking.send(ASK, data));
    }

    @Override
    @Deprecated
    public @Nullable ClientTardis demandTardis(MinecraftClient client, UUID uuid) {
        ClientTardis result = this.lookup.get(uuid);

        if (result == null)
            this.loadTardis(client, uuid, null);

        return result;
    }

    @Deprecated
    public @Nullable ClientTardis demandTardis(UUID uuid) {
        return this.demandTardis(MinecraftClient.getInstance(), uuid);
    }

    public void getTardis(UUID uuid, Consumer<ClientTardis> consumer) {
        this.getTardis(MinecraftClient.getInstance(), uuid, consumer);
    }

    private void syncTardis(UUID uuid, String json) {
        try {
            ClientTardis tardis = this.networkGson.fromJson(json, ClientTardis.class);
            Tardis.init(tardis, TardisComponent.InitContext.deserialize());

            tardis.travel(); // get a random element. if its null it will complain

            synchronized (this) {
                ClientTardis old = this.lookup.put(tardis);

                if (old != null)
                    old.age();

                for (Consumer<ClientTardis> consumer : this.subscribers.removeAll(uuid)) {
                    consumer.accept(tardis);
                }
            }
        } catch (Throwable t) {
            AITMod.LOGGER.error("Received malformed JSON file {}", json);
            AITMod.LOGGER.error("Failed to deserialize TARDIS data: ", t);
        }
    }

    private void syncTardis(PacketByteBuf buf) {
        this.syncTardis(buf.readUuid(), buf.readString());
    }

    private void syncBulk(PacketByteBuf buf) {
        int count = buf.readInt();

        for (int i = 0; i < count; i++) {
            this.syncTardis(buf);
        }
    }

    private void syncComponent(ClientTardis tardis, PacketByteBuf buf) {
        String rawId = buf.readString();

        TardisComponent.IdLike id = TardisComponentRegistry.getInstance().get(rawId);
        TardisComponent component = this.networkGson.fromJson(buf.readString(), id.clazz());

        id.set(tardis, component);
        TardisComponent.init(component, tardis, TardisComponent.InitContext.deserialize());
    }

    private void syncDelta(PacketByteBuf buf) {
        UUID id = buf.readUuid();
        int count = buf.readShort();

        ClientTardis tardis = this.demandTardis(id);

        if (tardis == null)
            return; // wait 'till the server sends a full update

        for (int i = 0; i < count; i++) {
            this.syncComponent(tardis, buf);
        }
    }

    @Override
    protected GsonBuilder createGsonBuilder(Exclude.Strategy strategy) {
        return super.createGsonBuilder(strategy)
                .registerTypeAdapter(ClientTardis.class, ClientTardis.creator());
    }

    @Override
    public void reset() {
        this.subscribers.clear();

        this.forEach(ClientTardis::dispose);
        super.reset();
    }

    public static ClientTardisManager getInstance() {
        return instance;
    }
}
