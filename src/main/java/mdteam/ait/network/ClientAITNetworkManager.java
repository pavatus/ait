package mdteam.ait.network;

import mdteam.ait.AITMod;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientAITNetworkManager {
    public static final Identifier SEND_REQUEST_ADD_TO_INTERIOR_SUBSCRIBERS = new Identifier(AITMod.MOD_ID, "send_request_add_to_interior_subscribers");
    public static final Identifier SEND_REQUEST_ADD_TO_EXTERIOR_SUBSCRIBERS = new Identifier(AITMod.MOD_ID, "send_request_add_to_exterior_subscribers");
    public static final Identifier SEND_EXTERIOR_UNLOADED = new Identifier(AITMod.MOD_ID, "send_exterior_unloaded");
    public static final Identifier SEND_INTERIOR_UNLOADED = new Identifier(AITMod.MOD_ID, "send_interior_unloaded");
    public static final Identifier SEND_REQUEST_EXTERIOR_CHANGE_FROM_MONITOR = new Identifier(AITMod.MOD_ID, "send_request_exterior_change_from_monitor");
    public static final Identifier SEND_SNAP_TO_OPEN_DOORS = new Identifier(AITMod.MOD_ID, "send_snap_to_open_doors");
    public static final Identifier SEND_REQUEST_FIND_PLAYER_FROM_MONITOR = new Identifier(AITMod.MOD_ID, "send_request_find_player_from_monitor");

    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((client, handler) -> ClientTardisManager.getInstance().reset());
    }

    public static void ask_for_interior_subscriber(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(SEND_REQUEST_ADD_TO_INTERIOR_SUBSCRIBERS, buf);
    }

    public static void ask_for_exterior_subscriber(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(SEND_REQUEST_ADD_TO_EXTERIOR_SUBSCRIBERS, buf);
    }

    public static void send_exterior_unloaded(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(SEND_EXTERIOR_UNLOADED, buf);
    }

    public static void send_interior_unloaded(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(SEND_INTERIOR_UNLOADED, buf);
    }

    public static void send_request_exterior_change_from_monitor(UUID uuid, ClientExteriorVariantSchema clientExteriorVariantSchema) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeString(clientExteriorVariantSchema.parent().id().toString());
        buf.writeString(clientExteriorVariantSchema.id().toString());
        ClientPlayNetworking.send(SEND_REQUEST_EXTERIOR_CHANGE_FROM_MONITOR, buf);
    }

    public static void send_snap_to_open_doors(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(SEND_SNAP_TO_OPEN_DOORS, buf);
    }

    public static void send_request_find_player_from_monitor(UUID uuid, UUID playerUUID) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeUuid(playerUUID);
    }
}
