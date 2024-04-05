package loqor.ait.client.util;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.registry.ClientDoorRegistry;
import loqor.ait.AITMod;
import loqor.ait.client.registry.ClientExteriorVariantRegistry;
import loqor.ait.core.AITDimensions;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisConsole;
import loqor.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import static loqor.ait.tardis.util.TardisUtil.*;

public class ClientTardisUtil {
	public static final int MAX_POWER_DELTA_TICKS = 3 * 20;
	public static final int MAX_ALARM_DELTA_TICKS = 60;
	private static int alarmDeltaTick;
	private static boolean alarmDeltaDirection; // true for increasing false for decreasing
	private static int powerDeltaTick;
	public static final Identifier CHANGE_SONIC = new Identifier(AITMod.MOD_ID, "change_sonic");

	public static void changeExteriorWithScreen(UUID uuid, String exterior, String variant, boolean variantchange) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(uuid);
		buf.writeString(exterior);
		buf.writeBoolean(variantchange);
		buf.writeString(variant);
		ClientPlayNetworking.send(CHANGE_EXTERIOR, buf);
	}

	public static void changeSonicWithScreen(UUID uuid, int sonic_type) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(uuid);
		buf.writeInt(sonic_type);
		ClientPlayNetworking.send(CHANGE_SONIC, buf);
	}

	public static void snapToOpenDoors(Tardis tardis) {
		snapToOpenDoors(tardis.getUuid());
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
		if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
			return false;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);

		return found != null;
	}

	/**
	 * Gets the tardis the player is currently inside
	 *
	 * @return
	 */
	public static Tardis getCurrentTardis() {
		if (!isPlayerInATardis()) return null;

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return null;
		Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);
		return found;
	}

	public static double distanceFromConsole() {
		if (!isPlayerInATardis()) return 0;

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return 0;

		Tardis tardis = getCurrentTardis();
		BlockPos pos = player.getBlockPos();

		double lowest = Double.MAX_VALUE;

		for (TardisConsole console : tardis.getDesktop().getConsoles()) {
			double distance = Math.sqrt(pos.getSquaredDistance(console.position()));
			if (distance < lowest) lowest = distance;
		}

		return lowest;
	}

	public static ExteriorModel getExteriorModel(Tardis tardis) {
		return ClientExteriorVariantRegistry.withParent(tardis.getExterior().getVariant()).model();
	}

	public static DoorModel getDoorModel(Tardis tardis) {
		return ClientDoorRegistry.withParent(tardis.getExterior().getVariant().door()).model();
	}

	public static void tickPowerDelta() {
		if (!isPlayerInATardis()) return;
		Tardis tardis = getCurrentTardis();

		if (tardis.hasPower() && getPowerDelta() < MAX_POWER_DELTA_TICKS) {
			setPowerDelta(getPowerDelta() + 1);
		} else if (!tardis.hasPower() && getPowerDelta() > 0) {
			setPowerDelta(getPowerDelta() - 1);
		}
	}

	public static int getPowerDelta() {
		if (!isPlayerInATardis()) return 0;
		return powerDeltaTick;
	}

	public static float getPowerDeltaForLerp() {
		return (float) getPowerDelta() / MAX_POWER_DELTA_TICKS;
	}

	public static void setPowerDelta(int delta) {
		if (!isPlayerInATardis()) return;
		powerDeltaTick = delta;
	}

	public static void tickAlarmDelta() {
		if (!isPlayerInATardis()) {
			if (getAlarmDelta() > 0)
				setAlarmDelta(0);
			return;
		}
		Tardis tardis = getCurrentTardis();

		if (!tardis.getHandlers().getAlarms().isEnabled()) {
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
		if (!isPlayerInATardis()) return 0;
		return alarmDeltaTick;
	}

	public static float getAlarmDeltaForLerp() {
		return (float) getAlarmDelta() / MAX_ALARM_DELTA_TICKS;
	}

	public static void setAlarmDelta(int delta) {
		if (!isPlayerInATardis()) return;
		alarmDeltaTick = delta;
	}
}
