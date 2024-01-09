package mdteam.ait.network;

import mdteam.ait.AITMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientAITNetworkManager {
    public static final Identifier ASK_FOR_INTERIOR_SUBSCRIBERS = new Identifier(AITMod.MOD_ID, "ask_for_interior_subscribers");
    public static final Identifier ASK_FOR_EXTERIOR_SUBSCRIBERS = new Identifier(AITMod.MOD_ID, "ask_for_exterior_subscribers");
    public static final Identifier SEND_EXTERIOR_UNLOADED = new Identifier(AITMod.MOD_ID, "send_exterior_unloaded");
    public static final Identifier SEND_INTERIOR_UNLOADED = new Identifier(AITMod.MOD_ID, "send_interior_unloaded");

    public static void init() {

    }

    public static void ask_for_interior_subscriber(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(ASK_FOR_INTERIOR_SUBSCRIBERS, buf);
    }

    public static void ask_for_exterior_subscriber(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(ASK_FOR_EXTERIOR_SUBSCRIBERS, buf);
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
}
