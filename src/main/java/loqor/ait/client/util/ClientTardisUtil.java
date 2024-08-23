package loqor.ait.client.util;

import static loqor.ait.tardis.util.TardisUtil.SNAP;

import java.util.UUID;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITDimensions;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.ClientTardis;

public class ClientTardisUtil {
    public static final int MAX_POWER_DELTA_TICKS = 3 * 20;
    public static final int MAX_ALARM_DELTA_TICKS = 60;
    private static int alarmDeltaTick;
    private static boolean alarmDeltaDirection; // true for increasing false for decreasing
    private static int powerDeltaTick;

    private static ClientTardis currentTardis;

    static {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null)
                return;

            ClientTardis newTardis = null;

            if (isPlayerInATardis(client))
                newTardis = (ClientTardis) TardisUtil.findTardisByInterior(client.player.getBlockPos(), false);

            currentTardis = newTardis;
        });
    }

    public static void changeExteriorWithScreen(UUID uuid, Identifier exterior, Identifier variant,
            boolean variantchange) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeBoolean(variantchange);
        buf.writeIdentifier(variant);
        ClientPlayNetworking.send(TardisExterior.CHANGE_EXTERIOR, buf);
    }

    public static void changeExteriorWithScreen(ClientTardis tardis, Identifier exterior, Identifier variant,
            boolean variantchange) {
        changeExteriorWithScreen(tardis.getUuid(), exterior, variant, variantchange);
    }

    public static void changeSonicWithScreen(UUID uuid, SonicSchema schema) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeIdentifier(schema.id());
        ClientPlayNetworking.send(TardisUtil.CHANGE_SONIC, buf);
    }

    public static void snapToOpenDoors(Tardis tardis) {
        snapToOpenDoors(tardis.getUuid());
    }

    public static void snapToOpenDoors(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(SNAP, buf);
    }

    public static boolean isPlayerInATardis() {
        return isPlayerInATardis(MinecraftClient.getInstance());
    }

    private static boolean isPlayerInATardis(MinecraftClient client) {
        return client.world != null && client.world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD;
    }

    /**
     * Gets the tardis the player is currently inside
     */
    public static ClientTardis getCurrentTardis() {
        return currentTardis;
    }

    public static double distanceFromConsole() {
        if (!isPlayerInATardis())
            return 0;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
            return 0;

        Tardis tardis = getCurrentTardis();

        if (tardis == null)
            return 0;

        BlockPos pos = player.getBlockPos();
        double lowest = Double.MAX_VALUE;

        for (BlockPos console : tardis.getDesktop().getConsolePos()) {
            double distance = Math.sqrt(pos.getSquaredDistance(console));

            if (distance < lowest)
                lowest = distance;
        }

        return lowest;
    }

    public static void tickPowerDelta() {
        Tardis tardis = getCurrentTardis();

        if (tardis == null)
            return;

        if (tardis.engine().hasPower() && getPowerDelta() < MAX_POWER_DELTA_TICKS) {
            setPowerDelta(getPowerDelta() + 1);
        } else if (!tardis.engine().hasPower() && getPowerDelta() > 0) {
            setPowerDelta(getPowerDelta() - 1);
        }
    }

    public static int getPowerDelta() {
        return currentTardis != null ? powerDeltaTick : 0;
    }

    public static float getPowerDeltaForLerp() {
        return (float) getPowerDelta() / MAX_POWER_DELTA_TICKS;
    }

    public static void setPowerDelta(int delta) {
        if (!isPlayerInATardis())
            return;

        powerDeltaTick = delta;
    }

    public static void tickAlarmDelta() {
        if (!isPlayerInATardis()) {
            if (getAlarmDelta() > 0)
                setAlarmDelta(0);
            return;
        }

        Tardis tardis = getCurrentTardis();

        if (!tardis.alarm().enabled().get()) {
            if (getAlarmDelta() != MAX_ALARM_DELTA_TICKS)
                setAlarmDelta(getAlarmDelta() + 1);
            return;
        }

        if (getAlarmDelta() < MAX_ALARM_DELTA_TICKS && alarmDeltaDirection) {
            setAlarmDelta(getAlarmDelta() + 1);
        } else if (getAlarmDelta() > 0 && !alarmDeltaDirection) {
            setAlarmDelta(getAlarmDelta() - 1);
        }

        if (getAlarmDelta() >= MAX_ALARM_DELTA_TICKS) {
            alarmDeltaDirection = false;
        }
        if (getAlarmDelta() == 0) {
            alarmDeltaDirection = true;
        }
    }

    public static int getAlarmDelta() {
        if (!isPlayerInATardis())
            return 0;
        return alarmDeltaTick;
    }

    public static float getAlarmDeltaForLerp() {
        return (float) getAlarmDelta() / MAX_ALARM_DELTA_TICKS;
    }

    public static void setAlarmDelta(int delta) {
        if (!isPlayerInATardis())
            return;
        alarmDeltaTick = delta;
    }
}
