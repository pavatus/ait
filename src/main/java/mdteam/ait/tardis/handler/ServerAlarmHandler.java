package mdteam.ait.tardis.handler;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;

import java.util.UUID;

// use this as reference for starting other looping sounds on the exterior
public class ServerAlarmHandler extends TardisLink {
    // fixme this is bad bad but i cant be assed with packets and thinking so hardcoding this value will be okay for now
    public static final int CLOISTER_LENGTH_TICKS = 3 * 20;
    private int soundCounter = 0; // decides when to start the next cloister sound

    public ServerAlarmHandler(UUID tardisId) {
        super(tardisId);
    }

    public void enable() {
        PropertiesHandler.setBool(getLinkedTardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, true);
        getLinkedTardis().markDirty();
    }

    public void disable() {
        PropertiesHandler.setBool(getLinkedTardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, false);
        getLinkedTardis().markDirty();
    }

    public boolean isEnabled() {
        return PropertiesHandler.getBool(getLinkedTardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if (!isEnabled()) return;

        soundCounter++;

        if (soundCounter >= CLOISTER_LENGTH_TICKS) {
            soundCounter = 0;
            getExteriorPos().getWorld().playSound(null, getExteriorPos(), AITSounds.CLOISTER, SoundCategory.AMBIENT, 0.5f, 0.5f);
        }
    }
}
