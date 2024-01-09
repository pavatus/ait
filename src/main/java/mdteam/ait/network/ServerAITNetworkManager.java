package mdteam.ait.network;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ServerAITNetworkManager {
    public static final Identifier SEND_TARDIS_DEMAT = new Identifier(AITMod.MOD_ID, "send_tardis_demat");
    public static final Identifier SEND_TARDIS_MAT = new Identifier(AITMod.MOD_ID, "send_tardis_mat");
    public static final Identifier SEND_EXTERIOR_CHANGED = new Identifier(AITMod.MOD_ID, "send_exterior_changed");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_ADD_TO_EXTERIOR_SUBSCRIBERS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            assert player != null;
            ServerTardisManager.getInstance().addExteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_ADD_TO_INTERIOR_SUBSCRIBERS, ((server, player, handler, buf, responseSender) -> {
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
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_SNAP_TO_OPEN_DOORS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            assert player != null;
            Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);
            if (tardis.getHandlers().getOvergrownHandler().isOvergrown()) return;
            player.getWorld().playSound(null, player.getBlockPos(), AITSounds.SNAP, SoundCategory.PLAYERS, 4f, 1f);
            if ((player.squaredDistanceTo(tardis.getDoor().getExteriorPos().getX(), tardis.getDoor().getExteriorPos().getY(), tardis.getDoor().getExteriorPos().getZ())) <= 200 || TardisUtil.inBox(tardis.getDesktop().getCorners().getBox(), player.getBlockPos())) {
                if (!player.isSneaking()) {
                    if(!tardis.getDoor().locked()) {
                        if (tardis.getDoor().isOpen()) tardis.getDoor().closeDoors();
                        else tardis.getDoor().openDoors();
                    }
                } else {
                    DoorHandler.toggleLock(tardis, player);
                }
            }
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_FIND_PLAYER_FROM_MONITOR, ((server, player, handler, buf, responseSender) -> {
            UUID tardisUUID = buf.readUuid();
            UUID playerUUID = buf.readUuid();
            Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisUUID);
            ServerPlayerEntity serverPlayer = TardisUtil.getServer().getPlayerManager().getPlayer(playerUUID);
            assert tardis.getDesktop().getCorners() != null;
            assert serverPlayer != null;
            tardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                    serverPlayer.getBlockX(),
                            serverPlayer.getBlockY(),
                            serverPlayer.getBlockZ(),
                            serverPlayer.getWorld(),
                            serverPlayer.getMovementDirection()),
                    PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND));
            TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 3f, 1f);
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
