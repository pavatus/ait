package mdteam.ait.network;

import mdteam.ait.AITMod;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
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
    public static final Identifier SEND_REQUEST_INTERIOR_CHANGE_FROM_MONITOR = new Identifier(AITMod.MOD_ID, "send_request_interior_change_from_monitor");

    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((client, handler) -> ClientTardisManager.getInstance().reset());
        ClientPlayNetworking.registerGlobalReceiver(ServerAITNetworkManager.SEND_EXTERIOR_ANIMATION_UPDATE_SETUP, ((client, handler, buf, responseSender) -> {
            int p = buf.readInt();
            UUID uuid = buf.readUuid();
            ClientTardisManager.getInstance().getTardis(uuid, (tardis -> {
                if (tardis == null || MinecraftClient.getInstance().world == null) return;
                BlockEntity blockEntity = MinecraftClient.getInstance().world.getBlockEntity(tardis.getExterior().getExteriorPos());
                if (!(blockEntity instanceof ExteriorBlockEntity exteriorBlockEntity)) return;
                exteriorBlockEntity.getAnimation().setupAnimation(TardisTravel.State.values()[p]);
            }));
        }));
    }

    public static void send_request_interior_change_from_monitor(UUID uuid, Identifier selected_interior) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeIdentifier(selected_interior);
        ClientPlayNetworking.send(SEND_REQUEST_INTERIOR_CHANGE_FROM_MONITOR, buf);
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

    public static void send_request_exterior_change_from_monitor(UUID uuid, ExteriorSchema exteriorSchema, ClientExteriorVariantSchema clientExteriorVariantSchema, boolean variantChange) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeString(exteriorSchema.id().toString());
        buf.writeString(clientExteriorVariantSchema.id().toString());
        buf.writeBoolean(variantChange);
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
