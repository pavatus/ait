package mdteam.ait.network;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ServerAITNetworkManager {
    public static final Identifier SEND_TARDIS_DEMAT = new Identifier(AITMod.MOD_ID, "send_tardis_demat");
    public static final Identifier SEND_TARDIS_MAT = new Identifier(AITMod.MOD_ID, "send_tardis_mat");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.ASK_FOR_EXTERIOR_SUBSCRIBERS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            assert player != null;
            ServerTardisManager.getInstance().addExteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.ASK_FOR_INTERIOR_SUBSCRIBERS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            assert player != null;
            ServerTardisManager.getInstance().addInteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_EXTERIOR_UNLOADED, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            assert player != null;
            ServerTardisManager.getInstance().removeExteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_INTERIOR_UNLOADED, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            assert player != null;
            ServerTardisManager.getInstance().removeInteriorSubscriberToTardis(player, uuid);
        }));
    }
}
