package mdteam.ait.network;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.compat.immersive.PortalsHandler;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
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
    public static final Identifier SEND_INTERIOR_DOOR_TYPE_CHANGED = new Identifier(AITMod.MOD_ID, "send_interior_door_type_changed");
    public static final Identifier SEND_EXTERIOR_ANIMATION_UPDATE_SETUP = new Identifier(AITMod.MOD_ID, "send_exterior_animation_update_setup");

    public static void init() {
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            ServerTardisManager.getInstance().removePlayerFromAllTardis(handler.getPlayer());
        }));

        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_ADD_TO_EXTERIOR_SUBSCRIBERS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            if (player == null) return;
            ServerTardisManager.getInstance().addExteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_ADD_TO_INTERIOR_SUBSCRIBERS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            if (player == null) return;
            ServerTardisManager.getInstance().addInteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_EXTERIOR_UNLOADED, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            if (player == null) return;
            ServerTardisManager.getInstance().removeExteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_INTERIOR_UNLOADED, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            if (player == null) return;
            ServerTardisManager.getInstance().removeInteriorSubscriberToTardis(player, uuid);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_EXTERIOR_CHANGE_FROM_MONITOR, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            Identifier exteriorIdentifier = Identifier.tryParse(buf.readString());
            Identifier variantIdentifier = Identifier.tryParse(buf.readString());
            boolean variantChanged = buf.readBoolean();
            Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);
            TardisExterior tardisExterior = tardis.getExterior();
            tardisExterior.setType(ExteriorRegistry.REGISTRY.get(exteriorIdentifier));
            if(DependencyChecker.hasPortals()) {
                PortalsHandler.removePortals(tardis);
            }
            if (variantChanged) {
                tardis.getExterior().setVariant(ExteriorVariantRegistry.REGISTRY.get(variantIdentifier));
            }
            WorldOps.updateIfOnServer(TardisUtil.getServer().getWorld(tardis.getTravel().getPosition().getWorld().getRegistryKey()), tardis.getDoor().getExteriorPos());
            WorldOps.updateIfOnServer(TardisUtil.getServer().getWorld(TardisUtil.getTardisDimension().getRegistryKey()), tardis.getDoor().getDoorPos());
            if (tardis.isGrowth()) {
                tardis.getHandlers().getInteriorChanger().queueInteriorChange(TardisItemBuilder.findRandomDesktop(tardis));
            }

        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_SNAP_TO_OPEN_DOORS, ((server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            if (player == null) return;
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
            if (tardis.getDesktop().getCorners() == null || serverPlayer == null) return;
            tardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                    serverPlayer.getBlockX(),
                            serverPlayer.getBlockY(),
                            serverPlayer.getBlockZ(),
                            serverPlayer.getWorld(),
                            serverPlayer.getMovementDirection()),
                    PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND));
            TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 3f, 1f);
        }));
        ServerPlayNetworking.registerGlobalReceiver(ClientAITNetworkManager.SEND_REQUEST_INTERIOR_CHANGE_FROM_MONITOR, ((server, player, handler, buf, responseSender) -> {
            Tardis tardis = ServerTardisManager.getInstance().getTardis(buf.readUuid());
            TardisDesktopSchema desktop = DesktopRegistry.get(buf.readIdentifier());
            if (tardis == null || desktop == null) return;
            tardis.getHandlers().getInteriorChanger().queueInteriorChange(desktop);
        }));
    }

    public static void setSendExteriorAnimationUpdateSetup(UUID tardisUUID, TardisTravel.State state) {
        Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisUUID);
        if (tardis == null) return;
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(state.ordinal());
        data.writeUuid(tardisUUID);
        if (!ServerTardisManager.getInstance().exterior_subscribers.containsKey(tardisUUID)) return;
        for (UUID player_uuid : ServerTardisManager.getInstance().exterior_subscribers.get(tardisUUID)) {
            ServerPlayerEntity player = TardisUtil.getServer().getPlayerManager().getPlayer(player_uuid);
            if (player == null) return;
            ServerPlayNetworking.send(player, SEND_EXTERIOR_ANIMATION_UPDATE_SETUP, data);
        }
    }
}
