package mdteam.ait.client.util;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.ClientDoorRegistry;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.*;

public class ClientTardisUtil {
    public static void changeExteriorWithScreen(UUID uuid, String exterior, String variant, boolean variantchange) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeString(exterior);
        buf.writeBoolean(variantchange);
        buf.writeString(variant);
        ClientPlayNetworking.send(CHANGE_EXTERIOR, buf);
    }

    public static void snapToOpenDoors(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        ClientPlayNetworking.send(SNAP, buf);
    }

    public static void setDestinationFromScreen(UUID tardisId, UUID playerUuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(tardisId);
        buf.writeUuid(playerUuid);
        ClientPlayNetworking.send(FIND_PLAYER, buf);
    }

    public static boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());

        return found != null;
    }

    /**
     * Gets the tardis the player is currently inside
     * @return
     */
    public static Tardis getCurrentTardis() {
        if (!isPlayerInATardis()) return null;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());
        return found;
    }

    public static double distanceFromConsole() {
        if (!isPlayerInATardis()) return 0;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return 0;

        Tardis tardis = getCurrentTardis();
        BlockPos console = tardis.getDesktop().getConsolePos();
        BlockPos pos = player.getBlockPos();

        if (console == null) console = pos;

        return Math.sqrt(pos.getSquaredDistance(console));
    }

    public static ExteriorModel getExteriorModel(Tardis tardis) {
        return ClientExteriorVariantRegistry.withParent(tardis.getExterior().getVariant()).model();
    }
    public static DoorModel getDoorModel(Tardis tardis) {
        return ClientDoorRegistry.withParent(tardis.getExterior().getVariant().door()).model();
    }
}
