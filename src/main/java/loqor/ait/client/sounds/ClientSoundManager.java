package loqor.ait.client.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;

import loqor.ait.client.sounds.alarm.ClientAlarmHandler;
import loqor.ait.client.sounds.fall.ClientFallSoundHandler;
import loqor.ait.client.sounds.flight.ClientFlightHandler;
import loqor.ait.client.sounds.hum.ClientCreakHandler;
import loqor.ait.client.sounds.hum.ClientHumHandler;
import loqor.ait.client.sounds.lava.ClientLavaSoundHandler;
import loqor.ait.client.sounds.rain.ClientRainSoundHandler;
import loqor.ait.client.sounds.sonic.ClientSonicSoundHandler;
import loqor.ait.client.sounds.vortex.ClientVortexSoundsHandler;

/**
 * A class for playing + managing our custom sounds on the client, right now
 * just a place to store the hum/alarm handlers
 */
@Environment(EnvType.CLIENT)
public class ClientSoundManager {
    private static ClientHumHandler hum;
    private static ClientAlarmHandler alarm;
    private static ClientFlightHandler flight;
    private static ClientCreakHandler creak;
    private static ClientVortexSoundsHandler vortexSounds;
    private static ClientRainSoundHandler rainSound;
    private static ClientLavaSoundHandler lavaSound;
    private static ClientSonicSoundHandler sonicSound;
    private static ClientFallSoundHandler fallSound;

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

    public static ClientRainSoundHandler getRainSound() {
        if (rainSound == null) {
            rainSound = ClientRainSoundHandler.create();
        }
        return rainSound;
    }

    public static ClientLavaSoundHandler getLavaSound() {
        if (lavaSound == null) {
            lavaSound = ClientLavaSoundHandler.create();
        }
        return lavaSound;
    }

    public static ClientSonicSoundHandler getSonicSound() {
        if (sonicSound == null) {
            sonicSound = ClientSonicSoundHandler.create();
        }
        return sonicSound;
    }

    public static ClientFallSoundHandler getFallSound() {
        if (fallSound == null) {
            fallSound = ClientFallSoundHandler.create();
        }
        return fallSound;
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

        if (getRainSound() != null)
            getRainSound().tick(client);

        if (getLavaSound() != null)
            getLavaSound().tick(client);

        if (getSonicSound() != null)
            getSonicSound().tick(client);

        if (getFallSound() != null)
            getFallSound().tick(client);
    }
}
