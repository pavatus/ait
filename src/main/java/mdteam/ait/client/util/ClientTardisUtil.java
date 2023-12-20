package mdteam.ait.client.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.CHANGE_EXTERIOR;
import static mdteam.ait.tardis.util.TardisUtil.SNAP;

public class ClientTardisUtil {
    public static void changeExteriorWithScreen(UUID uuid, int exterior, int variant, boolean variantchange) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeInt(exterior);
        buf.writeBoolean(variantchange);
        buf.writeInt(variant);
        ClientPlayNetworking.send(CHANGE_EXTERIOR, buf);
    }

    public static void snapToOpenDoors(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        ClientPlayNetworking.send(SNAP, buf);
    }

}
