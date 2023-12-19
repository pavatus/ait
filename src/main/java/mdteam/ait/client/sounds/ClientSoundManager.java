package mdteam.ait.client.sounds;

import mdteam.ait.tardis.handler.alarm.ClientAlarmHandler;
import mdteam.ait.tardis.handler.hum.ClientHumHandler;
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

    public static void tick(MinecraftClient client) {
        if (getAlarm() != null)
            getAlarm().tick(client);

        if (getHum() != null)
            getHum().tick(client);
    }
}
