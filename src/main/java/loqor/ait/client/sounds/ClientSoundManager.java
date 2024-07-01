package loqor.ait.client.sounds;

import loqor.ait.client.sounds.alarm.ClientAlarmHandler;
import loqor.ait.client.sounds.flight.ClientFlightHandler;
import loqor.ait.client.sounds.hum.ClientCreakHandler;
import loqor.ait.client.sounds.hum.ClientHumHandler;
import loqor.ait.client.sounds.vortex.ClientVortexSoundsHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
/**
 * A class for playing + managing our custom sounds on the client, right now just a place to store the hum/alarm handlers
 */
public class ClientSoundManager {
	private static ClientHumHandler hum;
	private static ClientAlarmHandler alarm;
	private static ClientFlightHandler flight;
	private static ClientCreakHandler creak;
	private static ClientVortexSoundsHandler vortexSounds;

	public static ClientHumHandler getHum() {
		if (hum == null) {
			hum = ClientHumHandler.create();
		}

		return hum;
	}

	public static ClientAlarmHandler getAlarm() {
		if (alarm == null) {
			alarm = ClientAlarmHandler.create();
		}

		return alarm;
	}

	public static ClientFlightHandler getFlight() {
		if (flight == null) {
			flight = ClientFlightHandler.create();
		}

		return flight;
	}

	public static ClientCreakHandler getCreaks() {
		if (creak == null) {
			creak = ClientCreakHandler.create();
		}
		return creak;
	}

	public static ClientVortexSoundsHandler getVortexSounds() {
		if (vortexSounds == null) {
			vortexSounds = ClientVortexSoundsHandler.create();
		}
		return vortexSounds;
	}


	public static void tick(MinecraftClient client) {
		if (getAlarm() != null)
			getAlarm().tick(client);

		if (getHum() != null)
			getHum().tick(client);

		if (getFlight() != null)
			getFlight().tick(client);

		if (getCreaks() != null)
			getCreaks().tick(client);

		if (getVortexSounds() != null)
			getVortexSounds().tick(client);
	}
}
