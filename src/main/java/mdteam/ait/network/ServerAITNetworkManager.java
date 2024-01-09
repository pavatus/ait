package mdteam.ait.network;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ServerAITNetworkManager {
    public static final Identifier SEND_TARDIS_DEMAT = new Identifier(AITMod.MOD_ID, "send_tardis_demat");
    public static final Identifier SEND_TARDIS_MAT = new Identifier(AITMod.MOD_ID, "send_tardis_mat");
    public static final Identifier SEND_EXTERIOR_CHANGED = new Identifier(AITMod.MOD_ID, "send_exterior_changed");

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
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_EXTERIOR_CHANGE_FROM_MONITOR, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            String json = buf.readString();
            ClientExteriorVariantSchema clientExteriorVariantSchema = ServerTardisManager.getInstance().getGson().fromJson(json, ClientExteriorVariantSchema.class);

            Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);
            TardisExterior tardisExterior = tardis.getExterior();
            tardisExterior.setType(ExteriorRegistry.REGISTRY.get(clientExteriorVariantSchema.id()));
            tardis.getExterior().setVariant(ExteriorVariantRegistry.REGISTRY.get(clientExteriorVariantSchema.parent().id()));
            WorldOps.updateIfOnServer(TardisUtil.getServer().getWorld(tardis.getTravel().getPosition().getWorld().getRegistryKey()), tardis.getDoor().getExteriorPos());
            if (tardis.isGrowth()) {
                tardis.getHandlers().getInteriorChanger().queueInteriorChange(TardisItemBuilder.findRandomDesktop(tardis));
            }
            setSendExteriorChanged(uuid);

        }));
    }

    public static void setSendExteriorChanged(UUID uuid) {
        Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);
        ExteriorVariantSchema exteriorVariantSchema = tardis.getExterior().getVariant();
        // serialize exteriorVariantSchema
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeString(ServerTardisManager.getInstance().getGson().toJson(exteriorVariantSchema));

        for (UUID player_uuid : ServerTardisManager.getInstance().exterior_subscribers.get(uuid)) {
            ServerPlayerEntity player = TardisUtil.getServer().getPlayerManager().getPlayer(player_uuid);
            assert player != null;
            ServerPlayNetworking.send(player, SEND_EXTERIOR_CHANGED, buf);
        }
    }
}
